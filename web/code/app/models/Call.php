<?php

namespace AllSOS\Models;

class Call extends MyMongo
{
    public $user = null;
    public $providers = [];
    public $assignedProvider = null;

    public function isClosed()
    {
        return $this->assignedProvider != null || count($providers) == 0;
    }

    public function providerRejects($id)
    {
        if ($this->isClosed()) {
            return false;
        }

        $key = array_search($id, $this->providers);

        if($key !== false){
            unset($this->providers[$key]);

            if (!$this->save()) {
                return false;
            }

            if(count($this->providers) == 0) {
                $user = User::findById((string)$this->user);
                Push::send('ups', [$user->registrationId]);
            }

            return true;
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
