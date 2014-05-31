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
$router->add('/user/changeinfo', "User::changeInfo")->setName('changeInfo');
$router->add('/user/changelocation', "User::changeLocation")->setName('changeLocation');
$router->add('/user/createcall', "Call::createCall")->setName('createCall');
$router->add('/provider/acceptcall', "Call::acceptCall")->setName('acceptCall');
$router->add('/provider/rejectscall', "Call::rejectsCall")->setName('rejectsCall');
$router->add('/user/getinfo', "User::getInfo")->setName('getInfo');
$router->add('/user/listAll',"User::listAll");

$router->add('/service/list',"Service::list");
$router->add('/service/add' ,"Service::add");

return $router;
