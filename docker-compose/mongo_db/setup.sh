#!/bin/bash
mongo <<EOF
use admin;
db.createUser({user:"admin",pwd:"admin",roles:[{role:'root',db:'admin'}]});
db.auth("admin","admin");
use test;
db.createUser({ user: 'test', pwd: 'test', roles: [{ role: "readWrite", db: "test" }] });
db.auth("test","test");
db.createCollection("test_collection");
EOF
# currently, do not import the init data json
#mongoimport --db datawarehouse --collection test_collection --file $WORKSPACE/init_collection1.json
#mongoimport --db datawarehouse --collection test_collection --file $WORKSPACE/init_collection2.json