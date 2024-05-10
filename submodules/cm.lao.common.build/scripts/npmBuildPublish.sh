#!/bin/sh
set -e
# This script build the vuejs project

node --version
npm --version
echo "running..."
npm install
npm run build
sleep 5
if [ -z "$(ls -A dist)" ]; then
   echo "Build failed. Therefore 'dist' directory is empty"
else
  echo "Publishing to npm..."
  npm publish
  echo "Published to npm..."
fi
