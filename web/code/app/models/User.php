<?php

namespace AllSOS\Models;

class User extends MyMongo
{
    
    public static function listAllUsers(){
        $users = User::find();
        echo "<table border='1'>";
        echo "<tr><td>email</td><td>password</td><td>tokens</td><td>services</td></tr>";
        foreach ($users as $key => $value) {
            echo "<tr>";
            echo "<td>".$value->email."</td>";
            echo "<td>".$value->password."</td>";
            echo "<td>".implode($value->tokens)."</td>"; 
            echo "<td>".implode($value->services)."</td>";
            echo "</tr>";
        }
        echo "</table>";
     }
    public static function listAllSuppliers(){
        $users = User::find();
        echo "<table border='1'>";
        echo "<tr><td>email</td><td>password</td><td>tokens</td><td>services</td></tr>";
        foreach ($users as $key => $value) {
            echo "<tr>";
            echo "<td>".$value->email."</td>";
            echo "<td>".$value->password."</td>";
            echo "<td>".implode($value->tokens)."</td>"; 
            echo "<td>".implode($value->services)."</td>";
            echo "</tr>";
        }
        echo "</table>";
    }
    public static function findByEmail($email){

    }

    public $email;
    public $password;
    public $tokens = [];
    public $services = [];
    public $location;
    public $range;

    public function logout($token){
        /*The token $token will be removed from the user variables*/
    }
    public function logoutAll(){
        /*Remove all tokens associated with the user*/
        
    }
    public function login($email,$password){
        /*The user is logged in... a token is created*/
    }


    


}
