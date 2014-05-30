<?php

namespace AllSOS\Models;

class Service extends MyMongo
{
    public $name;

 	public function initialize()
    {
        $this->ensureIndex(
            array('name' => 1),
            array('unique' => true, 'dropDups' => true)
        );
    }

    public function getServices(){

    	$service = Service::find();
    	return $service;
    }

}
