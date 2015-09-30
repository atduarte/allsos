#!/usr/bin/env bash

echo ">>> Main Server Configuration"

echo "> Base"
sudo bash base.sh

echo "> PHP"
sudo bash php.sh

echo "> Nginx"
sudo bash nginx.sh
sudo bash vhosts.sh

echo "> MongoDB"
sudo bash mongodb.sh

echo "> Composer"
sudo bash composer.sh

echo "> PHPUnit"
sudo bash phpunit.sh

echo "> Phalcon"
sudo bash phalconphp.sh
