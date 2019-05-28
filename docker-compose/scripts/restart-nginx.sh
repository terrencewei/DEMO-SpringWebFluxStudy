#!/usr/bin/env bash
. ./set-env.sh
sh stop-nginx.sh
sh start-nginx.sh
sh log-nginx.sh