<?php

namespace AllSOS\Controllers;

use AllSOS\Models\Service;

class ServiceController extends AjaxController{

	public function listAction(){
		$service = new Service();
		return $this->json( $service->getServices() );
	}
	public function addAction(){

		$newName = $this->request->getQuery("catName", "string", null);
		$service = new Service();
		$service->name = $newName;

		return $this->json( $service->save() );
	}

}