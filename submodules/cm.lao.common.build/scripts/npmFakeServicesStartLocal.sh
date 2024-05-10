#!/bin/sh

echo "starting fake services..."
productBaseDir=$(pwd)
echo $productBaseDir
cd ../fake-services
mkdir -p logs
npm run start
cd $productBaseDir
