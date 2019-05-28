#!/usr/bin/env bash
. ./set-env.sh
sh stop-app-spring-web-flux-study.sh
sh start-app-spring-web-flux-study.sh
sh log-app-spring-web-flux-study.sh