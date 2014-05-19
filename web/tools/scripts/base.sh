#!/usr/bin/env bash

echo ">>> Installing Base Packages"

# Update
sudo apt-get update -y

# Install base packages
sudo apt-get install -y git-core ack-grep vim tmux curl wget build-essential python-software-properties gcc autoconf

# Git Config and set Owner
#curl https://gist.github.com/fideloper/3751524/raw/.gitconfig > /home/vagrant/.gitconfig
#sudo chown vagrant:vagrant /home/vagrant/.gitconfig

# Bash Aliases
cp ../files/dot/.bash_aliases ~/.bash_aliases


# Firewall

sudo apt-get install -y ufw
# Defaults
sudo ufw default deny incoming
sudo ufw default allow outgoing
# Specifics
sudo ufw allow www
sudo ufw allow 443
sudo ufw allow ssh

sudo ufw enable
