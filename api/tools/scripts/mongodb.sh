#!/usr/bin/env bash

echo ">>> Installing MongoDB"

# Get key and add to sources
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list

# Update
sudo apt-get update

# Install MongoDB
sudo apt-get -y install mongodb-10gen

sudo pecl install mongo

sudo touch /etc/php5/conf.d/mongo.ini

# PHP activate
echo 'extension=mongo.so' | sudo tee /etc/php5/mods-available/mongo.ini

ln -s /etc/php5/mods-available/mongo.ini /etc/php5/fpm/conf.d/mongo.ini
ln -s /etc/php5/mods-available/mongo.ini /etc/php5/cli/conf.d/mongo.ini

# Restart
sudo service php5-fpm restart
sudo service nginx restart
