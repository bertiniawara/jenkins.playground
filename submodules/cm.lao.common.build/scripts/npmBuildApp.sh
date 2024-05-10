#!/bin/sh

# This script build the vuejs project

productBaseDir=$(pwd)

node --version
npm --version
echo "running npmBuildApp.sh ..."
npm cache clean --force
rm -rf node_modules
npm install
npm run test:unit
npm run test:e2e
if [ $1 = "runBuild" ]; then
  npm run build
fi
