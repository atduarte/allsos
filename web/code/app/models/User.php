<?php

namespace AllSOS\Models;

use Phalcon\Mvc\Model\Validator\StringLength as StringLengthValidator;
use Phalcon\Mvc\Model\Validator\Email as EmailValidator;

class User extends MyMongo
{
    public $email = null;
    public $password = null;
    public $tokens = [];
    public $services = [];
    public $location = null;
    public $range = null;
    public $_activeToken = null;

    public function logout()
    {
        if ($this->_activeToken == null) {
            return $this->logoutAll();
        }

        $found = false;
        foreach ($this->tokens as $i => $token) {
            if ($token == $this->_activeToken) {
                unset($this->tokens[$i]);
                $found = true;
            }
        }

        return $found;
    }

    public function logoutAll()
    {
        $this->tokens = [];
        return true;
    }

    public static function findLoggedIn($email, $token)
    {
        $user = User::findFirst([
            'conditions' => [
                'email' => $email,
                'token' => $token
            ]]);

        $user->_activeToken = $token;
    }

    public static function findAndLogin($email, $password, &$token)
    {
        if (!$email || !$password) {
            return false;
        }

        $user = User::findFirst([
            'conditions' => [
                'email' => $email
            ]]);

        if (!$user) {
            return false;
        }

        $token = $user->login($password);

        return $user;
    }

    public function login($password)
    {
        if (!password_verify($password, $this->passwordHash)) {
            return false;
        }

        // Create a Token
        $token = rand();
        $this->token[] = $token;

        if ($this->save()) {
            return $token;
        } else {
            return false;
        }
    }

    public function beforeValidationOnCreate()
    {
        if (!$this->password) {
            return false;
        }

        return true;
    }

    public function validation()
    {
        // Valid Email
        $this->validate(new EmailValidator(array(
                'field' => 'email'
        )));

        if ($this->password) {
            $this->validate(new StringLengthValidator(array(
                'field' => 'password',
                'max' => 50,
                'min' => 6
            )));
        }

        return $this->validationHasFailed() != true;
    }

    public function beforeSave()
    {
        if (isset($this->password)) {
            $this->passwordHash = password_hash($this->password, PASSWORD_BCRYPT);
            unset($this->password);
        }
    }


    public static function listAllUsers(){
        $users = User::find();

        echo "<table border='1'>";
        echo "<tr><td>email</td><td>password</td><td>tokens</td><td>services</td></tr>";
        foreach ($users as $key => $value) {
            echo "<tr>";
            echo "<td>".$value->email."</td>";
            echo "<td>"."</td>";
            echo "<td>".implode($value->tokens)."</td>";
            echo "<td>".implode($value->services)."</td>";
            echo "</tr>";
        }
        echo "</table>";
     }



}
