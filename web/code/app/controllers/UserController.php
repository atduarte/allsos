<?php

namespace AllSOS\Controllers;

use AllSOS\Models\User;

class UserController extends AjaxController
{
    protected $user;

    protected function getUser()
    {
        // Search for User
        $email = $this->request->getQuery("email", "email", null);
        $token = (int)$this->request->getQuery("token", "int", null);

        $this->user = User::findLoggedIn($email, $token);
    }

    public function changeInfoAction()
    {
        $this->getUser();

        if (!$this->user) {
            return $this->json(['success' => false, 'message' => 'Invalid Email-Token']);
        }

        // Change Data

        $fields = ['newEmail', 'password', 'services', 'range'];

        foreach ($fields as $field) {
            $this->user->{$field} = $this->request->getQuery($field, null, null);
        }

        if ($this->user->save()) {
            return $this->json(['success' => true]);
        } else {
            return $this->json(['success' => false, 'message' => 'Invalid Fields']);
        }
    }

    public function changeLocationAction()
    {
        $this->getUser();

        if (!$this->user) {
            return $this->json(['success' => false, 'message' => 'Invalid Email-Token']);
        }

        $lat = $this->request->getQuery('lat', "string", null);
        $lon = $this->request->getQuery('lon', "string", null);

        $this->user->location = [$lat, $lon];

        if ($this->user->save()) {
            return $this->json(['success' => true]);
        } else {
            return $this->json(['success' => false, 'message' => 'Invalid Location']);
        }
    }

    public function getInfoAction()
    {
        $this->getUser();

        if (!$this->user) {
            return $this->json(['success' => false, 'message' => 'Invalid Email-Token']);
        }

        $info = $this->user->toArray();

        unset($info['password']);
        unset($info['tokens']);
        unset($info['_id']);
        unset($info['_activeToken']);
        unset($info['passwordHash']);

        return $this->json($info);
    }

    public function signUpAction()
    {
        // Get infos from $_GET()
        $email = $this->request->getQuery("email", "email", null);
        $password = $this->request->getQuery("password", "string", null);
        $telephone = $this->request->getQuery("telephone", "int", null);

        $user = new User();
        $user->email = $email;
        $user->password = $password;
        $user->telephone = $telephone;

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
        $this->getUser();

        if (!$this->user) {
            return $this->json(['success' => false, 'message' => 'Invalid Fields']);
        }

        // Try Logout

        if ($this->user->logout()) {
            return $this->json(['success' => true]);
        } else {
            return $this->json(['success' => false]);
        }
    }

    public function logoutAllAction()
    {
        $this->getUser();

        if (!$this->user) {
            return $this->json(['success' => false, 'message' => 'Invalid Fields']);
        }

        // Try LogoutAll()

        if ($this->user->logoutAll()) {
            return $this->json(['success' => true]);
        } else {
            return $this->json(['success' => false]);
        }
    }

    public function listAllAction()
    {
        return $this->json(User::find());
        // User::listAllUsers();
    }
}
