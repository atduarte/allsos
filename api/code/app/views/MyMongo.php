<?php

namespace Notnull\Revis\Models;

use Notnull\Revis\Exceptions;

class MyMongo extends \Phalcon\Mvc\Collection
{
    public function ensureIndex(array $fields, array $args)
    {
        // Get the raw \MongoDB Connection
        $connection = $this->getConnection();

        // Get the \MongoCollection connection (with added dynamic collection name (thanks Phalcon))
        $collection = $connection->selectCollection($this->getSource());

        // A unique index
        $collection->ensureIndex($fields, $args);
    }

    public function save()
    {
        $result = parent::save();

        if (!$result) {
            throw new \Exception();
        }

        return true;
    }

    public function fromArray(array $data)
    {
        if (!$data) {
            return;
        }

        foreach ($data as $key => $value) {
            $this->{$key} = $value;
        }
    }

    public function requireField($name)
    {
        if (!isset($this->{$name}) || $this->{$name} === null) {
            throw new Exceptions\FieldRequired($name);
        }
    }

    public function uniqueField($name)
    {
        $result = self::findFirst([
            [
                '_id' => ['$ne' => $this->_id],
                $name => $this->{$name}
            ]
        ]);

        if ($result) {
            throw new Exceptions\NotUniqueField($name);
        }
    }

    public function numericField($name)
    {
        if (!is_numeric($this->{$name})) {
            throw new Exceptions\InvalidField($name);
        }
    }

    public function positiveField($name, $allowZero = true)
    {
        $this->numericField($name);

        if (!$allowZero && $this->{$name} == 0) {
            throw new Exceptions\InvalidField($name);
        }

        if ($this->{$name} < 0) {
            throw new Exceptions\InvalidField($name);
        }
    }
}
