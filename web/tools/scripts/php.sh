#!/usr/bin/env bash

# Add repo for latest PHP
sudo add-apt-repository -y ppa:ondrej/php5

# Update Again
sudo apt-get update

# Install PHP
sudo apt-get install -y php5-cli php5-fpm php5-dev

# Installing aditional extensions
sudo apt-get install -y php5-curl php5-gd php5-gmp php5-mcrypt php5-xdebug php5-memcached libpcre3-dev
sudo apt-get install -y memcached

# xdebug Config
#cat > /etc/php5/mods-available/xdebug.ini << EOF
#xdebug.scream=1
#xdebug.cli_color=1
#xdebug.show_local_vars=1
#EOF
