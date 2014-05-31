<?php

namespace AllSOS\Controllers;

use AllSOS\Models as Models;

class AjaxController extends \Phalcon\Mvc\Controller
{
    public function json($result)
    {
        $this->view->disable();
        $this->response->setContentType('application/json', 'UTF-8');
        echo json_encode($result);
        return false;
    }

    public function notFoundAction()
    {
        return $this->json(['success' => false, 'message' => '404 - Not Found']);
    }
}


