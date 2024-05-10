#!/bin/sh

echo "stopping fake services..."
productBaseDir=$(pwd)
echo $productBaseDir
cd ../fake-services
npm run stop
cd $productBaseDir
