#!/bin/sh
set -e
# This script build the vuejs project

node --version
npm --version
echo "running npmBuild.sh ..."
echo "Deleting the router config not using lazy loading ..."
rm -rf src/router/controller-build
echo "Router config not using lazy loading removed ..."
npm cache clean --force
rm -rf node_modules
rm package-lock.json
npm install
npm run build
