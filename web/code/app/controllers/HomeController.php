<?php

namespace AllSOS\Controllers;

use AllSOS\Models\User;

class HomeController extends AjaxController
{
    public function indexAction()
    {
        $user = new User();
        $user->username = "user" . rand();
        $user->password = "basgda" . rand();
        $user->cenas = "askjdhg". rand();
        $user->save();

        $users = User::find();

        $usersInfo = [];
        foreach ($users as $user) {
            $usersInfo[] = $user->toArray();
        }

        return $this->json($usersInfo);
    }
}
