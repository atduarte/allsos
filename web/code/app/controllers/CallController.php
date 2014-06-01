<?php

namespace AllSOS\Controllers;

use AllSOS\Models\Push;
use AllSOS\Models\Service;
use AllSOS\Models\User;
use AllSOS\Models\Call;

class CallController extends AjaxController
{
    public function createCallAction()
    {
        if (!$this->user) {
            return $this->json(['success' => false, 'message' => 'Invalid Email-Token']);
        }

        // Get Service

        $service = $this->request->getQuery("service", "string", null);

        if (!$service) {
            return $this->json(['success' => false, 'message' => 'Invalid Service']);
        }

        // Check User Location

        if (!$this->user->location) {
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

        $pushIds = [];

        $call = new Call();
        $call->service = $service;
        $call->user = $this->user->_id;
        foreach ($providers as $provider) {
            $call->providers[] = $provider->_id;
            $pushIds[] = $provider->registrationId;
        }
        $call->save();

        // Contact Providers
        Push::send('És uma puta', $pushIds);

        return $this->json(['success' => true]);
    }

    public function acceptCallAction()
    {
        if ($this->callReaction('providerAccepts', $call)) {
            // Return Info about User & Service
            $info['user'] = User::findById((string)$call->_id);
            $info['call'] = $call->toArray();
            return $this->json(['success' => true, 'message' => $info]);
        }

         return $this->json(['success' => false]);
    }

    public function rejectCallAction()
    {
        return $this->json(['success' => $this->callReaction('providerRejects', $call)]);
    }

    protected function callReaction($method, &$call)
    {
        if (!$this->user || !$this->user->isProvider()) {
            return $this->json(['success' => false, 'message' => 'Invalid Email-Token']);
        }

        $callId = $this->request->getQuery("call", "string", null);

        $call = Call::findById($callId);

        if (!$call || $call->isClosed()) {
            return $this->json(['success' => false, 'message' => 'Invalid Call']);
        }

        return $call->{$method}($this->user->_id);
    }

    public function listAllAction()
    {
        return $this->json(Call::find());
    }
}
