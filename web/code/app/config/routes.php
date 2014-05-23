<?php
/*
 * Define custom routes. File gets included in the router service definition.
 */
$router = new Phalcon\Mvc\Router(false);
$router->removeExtraSlashes(true);
$router->setDefaultNamespace('AllSOS\Controllers');

// Home
$router->add('/user/signup', "User::signup")->setName('signup');
$router->add('/user/login', "User::login")->setName('login');
$router->add('/user/logout', "User::logout")->setName('logout');
$router->add('/user/logoutall', "User::logoutAll")->setName('logoutAll');

$router->add('/listAll',"Home::listAll");
$router->add('/listAllSuppliers',"Home::listAllSuppliers");


return $router;
