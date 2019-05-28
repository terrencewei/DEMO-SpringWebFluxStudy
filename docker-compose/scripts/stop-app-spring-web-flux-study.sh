#!/usr/bin/env bash
. ./set-env.sh
set -e

# Remove existing containers
docker-compose stop app-spring-web-flux-study
docker-compose rm -f app-spring-web-flux-study

# Remove old docker images
sh remove-docker-image.sh app-spring-web-flux-study
