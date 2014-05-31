<?php

namespace AllSOS\Models;

class MyMongo extends \Phalcon\Mvc\Collection
{
    public function ensureIndex(array $fields, array $args = null)
    {
        // Get the raw \MongoDB Connection
        $connection = $this->getConnection();

        // Get the \MongoCollection connection (with added dynamic collection name (thanks Phalcon))
        $collection = $connection->selectCollection($this->getSource());

        // A unique index
        if ($args) {
            $collection->ensureIndex($fields, $args);
        } else {
            $collection->ensureIndex($fields);
        }
    }

    public function save()
    {
        try {
            return parent::save();
        } catch (\MongoException $e) {
            return false;
        }
    }
}
