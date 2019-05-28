#!/usr/bin/env bash
. ./set-env.sh
set -e
sudo rm -rf $DC_NGINX_PATH/cache/*
# Remove existing containers
docker-compose stop nginx
docker-compose rm -f nginx
