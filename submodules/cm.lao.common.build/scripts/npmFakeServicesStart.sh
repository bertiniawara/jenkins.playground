#!/bin/sh

echo "starting fake services..."
productBaseDir=$(pwd)
echo $productBaseDir
cd ../fake-services
npm cache clean --force
rm -rf node_modules
npm install
mkdir -p logs
npm run start
cd $productBaseDir
