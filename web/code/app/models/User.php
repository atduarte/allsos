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

    public function initialize()
    {
        $this->ensureIndex(
            array('email' => 1),
            array('unique' => true, 'dropDups' => true)
        );
    }

    public function logout()
    {
        if ($this->_activeToken == null) {
            return $this->logoutAll();
        }

        $this->tokens = array_diff($this->tokens, [$this->_activeToken]);

        return $this->save();
    }

    public function logoutAll()
    {
        $this->tokens = [];
        return $this->save();
    }

    public static function findLoggedIn($email, $token)
    {
        $user = User::findFirst([
            'conditions' => [
                'email' => $email,
                'tokens' => (int)$token
            ]]);

        if (!$user) {
            return false;
        }

        $user->_activeToken = $token;
        return $user;
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
        $this->tokens[] = (int)$token;

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

        if (isset($this->password) && $this->password) {
            $this->validate(new StringLengthValidator(array(
                'field' => 'password',
                'max' => 50,
                'min' => 3
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

        $this->tokens = array_values($this->tokens);
    }


    // public static function listAllUsers(){
    //     $users = User::find();
    //     echo "<table border='1'>";
    //     echo "<tr><td>email</td><td>password</td><td>tokens</td><td>services</td></tr>";
    //     foreach ($users as $key => $value) {
    //         echo "<tr>";
    //         echo "<td>".$value->email."</td>";
    //         echo "<td>".$value->password."</td>";
    //         echo "<td>".implode($value->tokens)."</td>";
    //         echo "<td>".implode($value->services)."</td>";
    //         echo "</tr>";
    //     }
    //     echo "</table>";
    //  }
    // public static function listAllSuppliers(){
    //     $users = User::find();
    //     echo "<table border='1'>";
    //     echo "<tr><td>email</td><td>password</td><td>tokens</td><td>services</td></tr>";
    //     foreach ($users as $key => $value) {
    //         echo "<tr>";
    //         echo "<td>".$value->email."</td>";
    //         echo "<td>".$value->password."</td>";
    //         echo "<td>".implode($value->tokens)."</td>";
    //         echo "<td>".implode($value->services)."</td>";
    //         echo "</tr>";
    //     }
    //     echo "</table>";
    // }


}
