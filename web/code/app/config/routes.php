<?php
/*
 * Define custom routes. File gets included in the router service definition.
 */
$router = new Phalcon\Mvc\Router(false);
$router->removeExtraSlashes(true);
$router->setDefaultNamespace('AllSOS\Controllers');

// Home
$router->add('/user/signup', "User::signup");
$router->add('/user/login', "User::login");
$router->add('/user/logout', "User::logout");
$router->add('/user/logoutall', "User::logoutAll");

$router->add('/user/getinfo', "User::getInfo");
$router->add('/user/changeinfo', "User::changeInfo");
$router->add('/user/changelocation', "User::changeLocation");

$router->add('/call/make', "Call::createCall");
$router->add('/call/accept', "Call::acceptCall");
$router->add('/call/reject', "Call::rejectCall");

$router->add('/service/list',"Service::list");
$router->add('/service/add' ,"Service::add");

$router->add('/user/listall',"User::listAll");

return $router;
