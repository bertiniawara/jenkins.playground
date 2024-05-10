#!/bin/bash
user=$1
password=$1
if [ -z "$2" ]
        then 
                port=5432
        else 
                port=$2
fi

createuser -p $port  $user
psql -U postgres -d postgres -c "alter user $user with password '$password';"

