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


    }
    public function listAllAction(){
        User::listAllUsers();

    }
    public function listAllSuppliersAction(){
        User::listAllSuppliers();
    }
    public function loginAction(){
        
    }


}
