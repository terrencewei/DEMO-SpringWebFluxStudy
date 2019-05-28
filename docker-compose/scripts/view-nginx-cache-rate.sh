#!/bin/bash
LOG_FILE='../nginx/log/access.log'
awk '{print $3}' $LOG_FILE  | sort | uniq -c | sort -r