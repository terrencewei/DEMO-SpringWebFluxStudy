#!/usr/bin/env bash
. ./set-env.sh
set -e
sh stop-app-spring-web-flux-study.sh
gradle -b ../../build.gradle clean bootJar
sh build-docker-image.sh app-spring-web-flux-study
docker-compose up -d app-spring-web-flux-study