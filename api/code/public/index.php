<?php

    ini_set("display_errors", 1);
    error_reporting(E_ALL);

try {

    //Register an autoloader
    $loader = new \Phalcon\Loader();

    $loader->registerNamespaces([
        'AllSOS\Controllers' => __DIR__ . '/../app/controllers/',
        'AllSOS\Models' => __DIR__ . '/../app/models/'
    ]);

    $loader->register();

    //Create a DI
    $di = new \Phalcon\DI\FactoryDefault();

    //Setting MongoDB
    $di->set('mongo', function () {
        $mongo = new MongoClient();
        return $mongo->selectDb("allsos");
    }, true);

    //Registering the collectionManager service
    $di->set('collectionManager', function () {
        return new Phalcon\Mvc\Collection\Manager();
    }, true);

    //Setting Router
    $di->set('router', function () {
        return require __DIR__ . '/../app/config/routes.php';
    });

    //Setting URL Helper
    $di->set('url', function () {
        $url = new Phalcon\Mvc\Url();
        return $url;
    });

    //Registering the view component
    $di->set('view', function(){
        $view = new \Phalcon\Mvc\View();
        $view->setViewsDir(__DIR__ . '/../app/views/');
        return $view;
    });

    // URL Creation
    $url = str_replace('?' . $_SERVER['QUERY_STRING'], '', $_SERVER['REQUEST_URI']);

    //Handle the request
    $application = new \Phalcon\Mvc\Application();
    $application->setDI($di);
    echo $application->handle($url)->getContent();

} catch (\Phalcon\Exception $e) {
     echo "PhalconException: ", $e->getMessage();
}
