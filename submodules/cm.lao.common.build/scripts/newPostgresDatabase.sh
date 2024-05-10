#!/bin/bash
user=$1
database=$2
if [ -z "$3" ]
        then 
                port=5432
        else 
                port=$3
fi

createdb -p $port -O $user $database
psql -U postgres -d $database -c "ALTER SCHEMA public OWNER TO $user;"

