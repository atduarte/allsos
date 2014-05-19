<?php

namespace Notnull\Revis\Models\UserVault;

use Phalcon\Mvc\Model\Validator\Email as EmailValidator;
use Phalcon\Mvc\Model\Validator\Regex as RegexValidator;
use Phalcon\Mvc\Model\Validator\StringLength as StringLengthValidator;
use Phalcon\Mvc\Model\Validator\Uniqueness as Uniqueness;

use Notnull\Revis\Models as Models;
use Notnull\Revis\Exceptions;

class Users extends Models\MyMongo
{
    const COOKIE_TTL = 31536000; // One Year
    const SESSION_TTL = 86400; // One Day
    const MAX_ACTIVESESSIONS = 10;

    public $email = null;
    public $sessions = [];
    public $payments = null;

    public function initialize()
    {
        // A unique index
        $this->ensureIndex(
            array('email' => 1),
            array('unique' => true, 'dropDups' => true)
        );
    }

    public function logout()
    {
        $cookies = $this->_dependencyInjector->get("cookies");

        if ($cookies->has("uservault-id") && $cookies->has("uservault-code")) {

            $id = $cookies->get('uservault-id')->getValue();
            $code = $cookies->get('uservault-code')->getValue();

            if ($id == $this->email && isset($this->sessions)) {
                foreach ($this->sessions as $i => $session) {
                    if ($session["code"] == $code) {
                        unset($this->sessions[$i]);
                    }
                }
            }
        }

        $this->save();

        $cookies->delete('uservault-id');
        $cookies->delete('uservault-code');
    }

    public static function findLoggedIn()
    {
        $cookies = \Phalcon\DI::getDefault()->get("cookies");

        if ($cookies->has("uservault-id") && $cookies->has("uservault-code")) {

            $id = (string)$cookies->get('uservault-id')->getValue();
            $code = (string)$cookies->get('uservault-code')->getValue();

            $user = Users::findFirst([[
                'email' => $id,
                'sessions' => [
                     '$elemMatch' => [
                         'code' => $code,
                         'expiration' => ['$gt' => time()]
                     ]
                ]
            ]]);

            return $user;
        }

        return null;
    }

    public static function findAndlogin($userAlias, $password, $remember = false)
    {
        if (!$userAlias) {
            throw new Exceptions\InvalidField("useralias");
        }

        if (!$password) {
            throw new Exceptions\InvalidField("password");
        }

        $user = Users::findFirst([
            'conditions' => [
                'email' => $userAlias
            ]]);

        if (!$user) {
            throw new Exceptions\InvalidField("useralias");
        }

        if (!self::verifyHash($password, $user->passwordHash)) {
            throw new Exceptions\InvalidField("password");
        }

        $user->saveSession($remember);

        return $user;
    }

    public function beforeValidationOnCreate()
    {
        $this->uniqueField("email");

        // Valid Password

        $this->validate(new StringLengthValidator(array(
                'field' => 'password',
                'max' => 50,
                'min' => 6
        )));

        if ($this->validationHasFailed()) {
            throw new Exceptions\InvalidField("password");
        }
    }

    public function validation()
    {
        // Valid Email

        $this->validate(new EmailValidator(array(
                'field' => 'email'
        )));

        if ($this->validationHasFailed()) {
            throw new Exceptions\InvalidField("email");
        }

        // Erase old sessions
        for($i = 0; $i < count($this->sessions); $i++) {
            if ($this->sessions[$i]["expiration"] <= time()) {
                array_splice($this->sessions, $i, 1);
                $i--;
            }
        }

        // Max 2 Sessions
        while (count($this->sessions) > self::MAX_ACTIVESESSIONS) {
            array_splice($this->sessions, 0, 1);
        }

        return true;
    }

    public function beforeSave()
    {
        if (isset($this->password)) {
            $this->passwordHash = $this->hash($this->password);
            unset($this->password);
        }
    }

    public function saveSession($remember)
    {
        $code = rand();

        if ($remember) {
            $expiration = time() + self::COOKIE_TTL;
        } else {
            $expiration = time() + self::SESSION_TTL;
        }

        $this->sessions[] = [
            "code" => (string) $code,
            "expiration" => (int) $expiration
        ];

        $this->save();

        $cookies = $this->_dependencyInjector->get("cookies");

        $expiration = $remember ? $expiration : 0;
        $cookies->set('uservault-id', $this->email, $expiration);
        $cookies->set('uservault-code', $code, $expiration);
    }

    private static function hash($password)
    {
        return password_hash($password, PASSWORD_BCRYPT);
    }

    private static function verifyHash($password, $hash)
    {
        return password_verify($password, $hash);
    }
}
