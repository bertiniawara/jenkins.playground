#!/bin/sh

# This script build the vuejs project

node --version
npm --version
echo "running..."
npm cache clean --force
rm -rf node_modules
npm install
npm run build
npm run test
