#!/bin/sh

# This script clear a schema in a postgres database on postgres instance installed on this machine
# 1. parameter: user name
# 2. parameter: password
# 3. parameter: hostname
# 4. parameter: port
# 5. parameter: database
# 6. parameter: schema

echo "clearing the schema '$6' of the database '$5' on server '$3':'$4'using user '$1'..."

export PGUSER=$1
export PGHOST=$3
export PGPORT=$4
export PGDATABASE=$5
export schemaToDelete=$6
export PGPASSFILE="./.pgpass"

echo "*:*:$PGDATABASE:$PGUSER:$2" > $PGPASSFILE
chmod 600 $PGPASSFILE

psql -c "DROP SCHEMA IF EXISTS $schemaToDelete CASCADE" 
psql -c "CREATE SCHEMA $schemaToDelete AUTHORIZATION \"$PGUSER\""

