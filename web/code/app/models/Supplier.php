<?php

namespace AllSOS\Models;

class Supplier extends User
{
    public $email;
    public $password;
    public $tokens = [];
    public $services = [];
    public $location;
    public $range;

    public function logout()
    {
    	echo "fez logout";
    }

    public static function findByToken($token)
    {
    	$user = Users::findFirst([
    		'conditions' => [
    			'token' => $token
    		]
    	]);

    	return $user;
    }


}
