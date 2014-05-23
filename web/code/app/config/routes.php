<?php
/*
 * Define custom routes. File gets included in the router service definition.
 */
$router = new Phalcon\Mvc\Router(false);
$router->removeExtraSlashes(true);
$router->setDefaultNamespace('AllSOS\Controllers');

// Home
$router->add('/', "Home::index")->setName('home');
$router->add('/login', "Home::login");
$router->add('/listAll',"Home::listAll");
$router->add('/listAllSuppliers',"Home::listAllSuppliers");


return $router;
