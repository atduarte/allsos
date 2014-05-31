<?php

namespace AllSOS\Controllers;

use AllSOS\Models\Service;
use AllSOS\Models\User;
use AllSOS\Models\Call;

class CallController extends AjaxController
{

    public function createCallAction()
    {
        // Search for User
        $email = $this->request->getQuery("email", "email", null);
        $token = (int)$this->request->getQuery("token", "int", null);

        $user = User::findLoggedIn($email, $token);

        if (!$user) {
            return $this->json(['success' => false, 'message' => 'Invalid Email-Token']);
        }

        // Get Service

        $service = $this->request->getQuery("service", "string", null);

        if (!$service) {
            return $this->json(['success' => false, 'message' => 'Invalid Service']);
        }

        // Check User Location

        if (!$user->location) {
            return $this->json(['success' => false, 'message' => 'Invalid Location']);
        }

        // Search Providers

        $lat = $this->request->getQuery("lat", "string", null);
        $lon = $this->request->getQuery("lon", "string", null);

        $providers = User::find([
            'conditions' => [
                'location' => ['$near' => [$lat, $lon]],
                'services' => $service
            ],
            'limit' => 10
        ]);

        if (!$providers) {
            return $this->json(['success' => false, 'message' => 'Providers not found']);
        }

        $call = new Call();
        $call->user = $user->_id;
        foreach ($providers as $provider) {
            $call->providers[] = $provider->_id;
        }
        $call->save();

        return $this->json($call);
    }

    protected function callReaction($method)
    {
        // Search for User
        $email = $this->request->getQuery("email", "email", null);
        $token = (int)$this->request->getQuery("token", "int", null);

        $user = User::findLoggedIn($email, $token);

        if (!$user || !$user->isProvider()) {
            return $this->json(['success' => false, 'message' => 'Invalid Email-Token']);
        }

        $callId = $this->request->getQuery("call", "string", null);

        $call = Call::findById($callId);

        if (!$call || $call->isClosed()) {
            return $this->json(['success' => false, 'message' => 'Invalid Call']);
        }

        return $this->json(['success' => $call->{$method}($user->_id)]);
    }

    public function acceptCallAction()
    {
        $this->callReaction('providerAccepts');
    }

    public function rejectsCallAction()
    {
        $this->callReaction('providerRejects');
    }
}
