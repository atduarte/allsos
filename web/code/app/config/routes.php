<?php
/*
 * Define custom routes. File gets included in the router service definition.
 */
$router = new Phalcon\Mvc\Router(false);
$router->removeExtraSlashes(true);
$router->setDefaultNamespace('AllSOS\Controllers');

// Home
$router->add('/', "Home::index")->setName('home');

// 404 Not Found
$router->notFound(array(
    'controller' => 'home',
    'action' => 'notFound'
));

return $router;
