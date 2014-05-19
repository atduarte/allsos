<?php

namespace AllSOS\Models;

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
}
