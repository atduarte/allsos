<?php

namespace AllSOS\Controllers;

use AllSOS\Models\User;

class UserController extends AjaxController
{
    public function signUpAction()
    {
        // Get infos from $_GET()

        $email = $this->request->getQuery("email", "email", null);
        $password = $this->request->getQuery("password", "string", null);

        $user = new User();
        $user->email = $email;
        $user->password = $password;

        if ($user->save()) {
            $token = $user->login($password);
            return $this->json(['success' => true, 'token' => $token]);
        } else {
            return $this->json(['success' => false, 'message' => 'Error Creating User']);
        }
    }

    public function loginAction()
    {
        // Get infos from $_GET()

        $email = $this->request->getQuery("email", "email", null);
        $password = $this->request->getQuery("password", "string", null);

        if (!$email || !$password) {
            return $this->json(['success' => false, 'message' => 'Missing Fields']);
        }

        // Try Login

        $user = User::findAndLogin($email, $password, $token);

        if ($user) {
            return $this->json(['success' => true, 'token' => $token]);
        } else {
            return $this->json(['success' => false, 'message' => 'Invalid Fields']);
        }
    }

    public function logoutAction()
    {
        // Get infos from $_GET()

        $email = $this->request->getQuery("email", "email", null);
        $token = (int)$this->request->getQuery("token", "int", null);

        // Search for User

        $user = User::findLoggedIn($email, $token);

        if (!$user) {
            return $this->json(['success' => false, 'message' => 'Invalid Fields']);
        }

        // Try Logout

        if ($user->logout()) {
            return $this->json(['success' => true]);
        } else {
            return $this->json(['success' => false]);
        }
    }

    public function logoutAllAction()
    {
        // Get infos from $_GET()

        $email = $this->request->getQuery("email", "email", null);
        $token = (int)$this->request->getQuery("token", "int", null);

        // Search for User

        $user = User::findLoggedIn($email, $token);

        if (!$user) {
            return $this->json(['success' => false, 'message' => 'Invalid Fields']);
        }

        // Try LogoutAll()

        if ($user->logoutAll()) {
            return $this->json(['success' => true]);
        } else {
            return $this->json(['success' => false]);
        }
    }

    public function listAllAction()
    {
        // $users = User::find();
        // foreach ($users as $user) {
        //     $user->delete();
        // }
        return $this->json(User::find());
        //User::listAllUsers();
    }

    public function listAllSuppliersAction()
    {
        //User::listAllSuppliers();
    }



}
