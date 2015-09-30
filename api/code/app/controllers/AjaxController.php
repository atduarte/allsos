<?php

namespace AllSOS\Controllers;

use AllSOS\Models as Models;
use AllSOS\Models\User;

class AjaxController extends \Phalcon\Mvc\Controller
{
    public function initialize()
    {
        // Search for User
        $email = $this->request->getQuery("email", "email", null);
        $token = (int)$this->request->getQuery("token", "int", null);

        $this->user = User::findLoggedIn($email, $token);
    }

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
