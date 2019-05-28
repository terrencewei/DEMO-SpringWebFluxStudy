#!/usr/bin/env bash
. ./set-env.sh
#docker-compose logs -f nginx
tail -f ../nginx/log/access.log