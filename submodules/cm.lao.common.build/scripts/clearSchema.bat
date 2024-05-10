@ECHO OFF

rem This script clear a schema in a postgres database on postgres instance installed on this machine
rem 1. parameter: user name
rem 2. parameter: password
rem 3. parameter: hostname
rem 4. parameter: port
rem 5. parameter: database
rem 6. parameter: schema

echo clearing the schema '%6' of the database '%5' on server '%3':'%4'using user '%1'...

set PGUSER=%1
set PGPASSWORD=%2
set PGHOST=%3
set PGPORT=%4
set PGDATABASE=%5
set schemaToDelete=%6

psql -c "DROP SCHEMA IF EXISTS %schemaToDelete% CASCADE" 
psql -c "CREATE SCHEMA %schemaToDelete% AUTHORIZATION %PGUSER%"

