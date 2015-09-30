<?php

namespace Notnull\Revis\Models\Works;

use Notnull\Revis\Exceptions;
use Notnull\Revis\Models;
use Notnull\Revis\Models\MySubMongo;

class Contracts extends MySubMongo
{
    public $value;
    public $bidDate = null;
    public $consignmentDate;
    public $term;
    public $extensions = [];
    public $paymentsPlan = [];
    public $autos = [];

    public function validationInitial()
    {
        $this->requireField("bidDate");
        $this->validation();
    }

    public function validation()
    {
        // Verify Value
        $this->requireField("value");
        if (!is_numeric($this->value) || $this->value <= 0) {
            throw new Exceptions\InvalidField("value");
        }

        // Verify Consignment Date
        $this->requireField("consignmentDate");
        if (!is_numeric($this->consignmentDate) || $this->consignmentDate <= 0) {
            throw new Exceptions\InvalidField("consignmentDate");
        }

        // Verify Bid Date
        if (isset($this->bidDate)) {
            if (!is_numeric($this->bidDate) || $this->bidDate <= 0) {
                throw new Exceptions\InvalidField("bidDate");
            }

            if ($this->bidDate > $this->consignmentDate) {
                throw new Exceptions\InvalidField("consignmentDate");
            }
        }

        // Verify Term

        $validTermType = function ($type) {
            return $type == 'M' || $type == 'W' || $type == 'D';
        };

        $this->requireField("term");
        if (!isset($this->term["number"]) || !is_numeric($this->term["number"]) || $this->term["number"] <= 0 ||
            !isset($this->term["type"]) || !$validTermType($this->term["type"])) {
            throw new Exceptions\InvalidField("term");
        }

        // Verify Extensions
        if (!isset($this->extensions)) {
            $this->extensions = [];
        }
        for ($i = 0; $i < count($this->extensions); $i++) {
            if (!isset($this->extensions[$i]["number"]) ||
                !is_numeric($this->extensions[$i]["number"]) || $this->extensions[$i]["number"] <= 0 ||
                !isset($this->extensions[$i]["type"]) || !$validTermType($this->extensions[$i]["type"])
            ) {
                array_splice($this->extensions, $i, 1);
                $i--;
            }
        }

        foreach ($this->extensions as $extension) {
            if (!isset($extension["number"]) || !isset($extension["type"])) {
                throw new Exceptions\InvalidField("extension");
            }
        }

        // Verify Payments Plan
        if (!isset($this->paymentsPlan)) {
            $this->paymentsPlan = [];
        }
        $total = 0;
        foreach ($this->paymentsPlan as $key => &$value) {
            if (!is_numeric($key) || $key <= 0) {
                throw new Exceptions\InvalidField("paymentsPlan");
            }

            if ($value == null) {
                $value = 0;
            }
            if (!is_numeric($value) || $value < 0) {
                throw new Exceptions\InvalidField("paymentsPlan");
            }

            $total += $value;
        }
        unset($value);

        // Verify PaymentsPlanTotal
        if ($total == 0) {
            $this->paymentsPlan = [];
        } elseif ($total != $this->value) {
            throw new Exceptions\InvalidField("paymentsPlan");
        }

        // Verify Autos
        if (!isset($this->autos)) {
            $this->autos = [];
        }
        foreach ($this->autos as $autoInfo) {
            $auto = new Autos;
            $auto->fromArray($autoInfo);
            $auto->validation();

            // Auto.Dates after Contract.ConsignmentDate

            if ($auto->creationDate < $this->consignmentDate) {
                throw new Exceptions\InvalidField('creationDate');
            }

            if (Models\Date::lastSecondOfMonth($auto->referredMonth) < $this->consignmentDate) {
                throw new Exceptions\InvalidField('referredMonth');
            }
        }

        // Verify Autos Total
        $total = 0;
        foreach ($this->autos as $auto) {
            $total += $auto["value"];
        }
        if ($this->value < $total) {
            throw new Exceptions\InvalidField('value');
        }

        return true;
    }

}
