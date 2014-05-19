<?php

namespace Notnull\Revis\Models;

use Notnull\Revis\Exceptions;
use Phalcon\Http\Client\Exception;
use Phalcon\Utils\Slug;

class Works extends MyMongo
{
    public $name;
    public $slug;
    public $owner;
    public $formulaId;
    public $location;
    public $authorId;
    public $contracts = [];

    // Return EndDate as Timestamp
    public function getContractEndDate($i)
    {
        if (!isset($this->contracts[$i])) {
            return null;
        }

        $contract = $this->contracts[$i];

        $date = new \DateTime();
        $date->setTimestamp($contract["consignmentDate"]);


        $extensions = $contract["extensions"];
        $extensions[] = $contract["term"];
        foreach ($extensions as $extension) {
            if (isset($extension["number"]) && isset($extension["type"])) {
                try {
                    $date->add(new \DateInterval('P' . $extension["number"] . $extension["type"]));
                } catch (\Exception $e) {
                }
            }
        }

        return $date->getTimestamp();
    }

    public function getEndDate()
    {
        $endDate = 0;
        foreach ($this->contracts as $i => $contract) {
            $tmp = $this->getContractEndDate($i);
            if ($tmp > $endDate) {
                $endDate = $tmp;
            }
        }

        return $endDate;
    }

    public function removeAuto($index)
    {
        $i = 0;
        foreach ($this->contracts as &$workContract) {
            foreach ($workContract["autos"] as $j => $auto) {
                if ($i == $index) {
                    array_splice($workContract["autos"], $j, 1);
                    return true;
                }
                $i++;
            }
        }

        return false;
    }

    public function getAutos()
    {
        $autos = null;
        foreach ($this->contracts as $contract) {
            if (!$autos) {
                $autos = $contract['autos'];
            } else {
                $autos = array_merge([$autos], $contract['autos']);
            }
        }

        return $autos;
    }

    public function getNextAutoName()
    {
        $i = 0;

        if (!isset($this->contracts) || count($this->contracts) == 0) {
            return ($i+1);
        }


        foreach ($this->contracts as $contract) {
            if (!isset($contract["autos"]) || count($contract["autos"]) == 0) {
                continue;
            }

            foreach ($contract["autos"] as $auto) {
                if (is_numeric($auto["name"]) && $auto["name"] > $i) {
                    $i = $auto["name"];
                }
            }
        }

        return ($i+1);
    }

    public function save(UserVault\Users $user = null)
    {
        // TODO: Check Permission
        if (!$user) {
            throw new Exceptions\MissingPermission();
        }

        parent::save();
    }

    public function delete(UserVault\Users $user = null)
    {
        // TODO: Check Permission
        if (!$user) {
            throw new Exceptions\MissingPermission();
        }

        parent::delete();
    }

    public function beforeValidation()
    {
        $this->slug = Slug::generate($this->name);

        if (!isset($this->formulaId) || !$this->formulaId) {
            $this->formulaId = "";
        }
    }

    public function validation()
    {
        // Verify Name
        $this->requireField("name");
        if (strlen($this->name) < 1) {
            throw new Exceptions\InvalidField("name");
        }

        // Verify Location
        $this->requireField("location");
        $this->location = (int)$this->location;
        if ($this->location !== 0 && $this->location !== 1) {
            throw new Exceptions\InvalidField("location");
        }

        // Verify Author
        $this->requireField("authorId");
        $author = UserVault\Users::findById($this->authorId);
        if (!isset($author) || !$author) {
            throw new Exceptions\InvalidField("authorId");
        }

        // Verify FormulaId
        if ($this->formulaId) {
            $formula = PredefinedFormulas::findById($this->formulaId);
            if (!isset($formula) || !$formula) {
                throw new Exceptions\InvalidField("formulaId");
            }
        }

        // Verify Contracts
        $autoNames = [];
        foreach ($this->contracts as $i => &$contractInfo) {
            $contract = new Works\Contracts;
            $contract->fromArray($contractInfo);
            if ($i == 0) {
                $contract->validationInitial();
            } else {
                $contract->validation();
            }

            // ConsignmentDate >= (BidDate || Initial->BidDate)

            $bidDate = $contract->bidDate ? $contract->bidDate :  $this->contracts[0]["bidDate"];
            if ($contract->consignmentDate < $bidDate) {
                throw new Exceptions\InvalidField("consignmentDate");
            }

            // Verify unique name of Autos
            foreach ($contract->autos as $auto) {
                if (in_array($auto["name"], $autoNames)) {
                    throw new Exceptions\NotUniqueField("autoName");
                }
                $autoNames[] = $auto["name"];
            }

            $contractInfo = $contract->toArray();
        }

        return true;
    }
}
