<?php

namespace AllSOS\Models;

class Call extends MyMongo
{
    public $user = null;
    public $providers = [];
    public $assignedProvider = null;

    public function isClosed()
    {
        return $this->assignedProvider != null;
    }

    public function providerRejects($id)
    {
        if ($this->isClosed()) {
            return false;
        }

        $key = array_search($id, $this->providers);

        if($key !== false){
            unset($this->providers[$key]);
            return $this->save();
        }

        return false;
    }

    public function providerAccepts($id)
    {
        if ($this->isClosed()) {
            return false;
        }

        $this->assignedProvider = $id;
        $this->providers = [];

        return $this->save();
    }

}
