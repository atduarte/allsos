#!/usr/bin/env bash

echo ">>> Configuring Nginx"

# Disable "default", enable "vagrant"
sudo rm -rf /etc/nginx/sites-enabled/*

# Virtual Hosts
FILES=../files/vhosts/*
for f in $FILES
do
  fn=$(basename $f)
  echo $fn
  sudo cp $f /etc/nginx/sites-available/$fn
  sudo ln -s /etc/nginx/sites-available/$fn /etc/nginx/sites-enabled/$fn
done

sudo service php5-fpm restart
sudo service nginx restart
