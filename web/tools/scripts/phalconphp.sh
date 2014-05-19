#!/usr/bin/env bash

echo ">>> Installing PhalconPHP"

git clone git://github.com/phalcon/cphalcon.git
cd cphalcon
git checkout phalcon-v1.3.1
cd build
sudo ./install
cd ../../
rm -R -f cphalcon

# PHP activate

echo 'extension=phalcon.so' | sudo tee /etc/php5/mods-available/phalcon.ini

ln -s /etc/php5/mods-available/phalcon.ini /etc/php5/fpm/conf.d/phalcon.ini
ln -s /etc/php5/mods-available/phalcon.ini /etc/php5/cli/conf.d/phalcon.ini

# Restart
sudo service php5-fpm restart
sudo service nginx restart
