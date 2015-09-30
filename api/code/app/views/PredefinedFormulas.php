<?php

namespace Notnull\Revis\Models;


use Notnull\Revis\Exceptions;


class PredefinedFormulas extends MyMongo
{
    public $name = null;

    public $slug = null;

    public $location = [];

    public $constant = 0.1;

    public $coefs = [];
 // Coeficientes de Materiais, Equipamentos de Apoio e Mão de Obra

    public function validation()
    {
        // Required Fields

        $this->requireField("name");

        $this->requireField("slug");

        $this->requireField("location");

        $this->requireField("constant");

        $this->requireField("coefs");


        // Unique Fields

        $this->uniqueField("slug");


        // Check Location

        if (!is_array($this->location) || count($this->location) == 0 || !Settings::isValidLocation($this->location)) {
            throw new Exceptions\InvalidField("location");

        }

        if (is_array($this->location)) {
            foreach ($this->location as $key => $value) {
                $this->location[$key] = intval($value);

            }
        } else {
            $this->location = intval($this->location);

        }

        // Check Constant

        if (!is_numeric($this->constant) || $this->constant < 0 || $this->constant > 1) {
            throw new Exceptions\InvalidField("constant");

        }

        // Check Coefs & Sum

        if (!is_array($this->coefs)) {
            $this->coefs = [];

        }

        $coefsSum = 0;

        $coefsSum += $this->constant;

        foreach ($this->coefs as $reference => $value) {

            // Check Id

            if (!IndexNames::find([['slug' => $reference]])) {
                throw new Exceptions\InvalidField("coefs");

            }

            // Check Value

            if (!is_numeric($value) || $value < 0 || $value > 1) {
                throw new Exceptions\InvalidField("coefs");

            }

            $coefsSum += $value;

        }

        $coefsSum = round($coefsSum, 2);

        if ($coefsSum != 1) {
            throw new Exceptions\InvalidField("coefsSum");

        }
    }
}
