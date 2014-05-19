<?php

namespace Notnull\Revis\Models;

use Phalcon\Mvc\Model\Validator\Regex as RegexValidator;
use Notnull\Revis\Exceptions;

class IndexValues extends MyMongo
{
    public $month = null;
    public $indexes = null;
    public $location = null;

    public function initialize()
    {
        // A unique index
        $this->ensureIndex(
            array('month' => 1),
            array('unique' => true, 'dropDups' => true)
        );
    }

    public function validation()
    {
        // Required Fields

        $this->requireField("month");
        $this->uniqueField("month");

        $this->requireField("location");
        $this->requireField("indexes");

        // Check Month

        $this->positiveField('month');

        if ($this->validationHasFailed()) {
            throw new Exceptions\InvalidField('month');
        }

        // Check Indexes

        if (!is_array($this->indexes) || count($this->indexes) == 0) {
            throw new Exceptions\InvalidField('indexes');
        }

        foreach ($this->indexes as $reference => $value) {

            // Check Id

            if (!IndexNames::find([['slug' => $reference]])) {
                throw new Exceptions\InvalidField('indexes');
            }

            // Check Value

            if (!is_numeric($value) || $value < 0) {
                throw new Exceptions\InvalidField('indexes');
            }
        }
    }
}
