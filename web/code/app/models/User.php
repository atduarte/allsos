<?php

namespace AllSOS\Models;

class User extends MyMongo
{
    public $username;
    public $password;
    public $services = [];
    public $location;
    public $range;
}
