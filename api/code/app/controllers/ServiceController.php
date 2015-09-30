<?php

namespace AllSOS\Controllers;

use AllSOS\Models\Service;

class ServiceController extends AjaxController
{

	public function listAction()
	{
		return $this->json(Service::find());
	}

	public function addAction()
	{
		$newName = $this->request->getQuery("name", "string", null);

		if (!$newName) {
			return $this->json(['success' => false, 'message' => 'Missing Name']);
		}

		$service = new Service();
		$service->name = $newName;

		return $this->json(['success' => $service->save()]);
	}

}
