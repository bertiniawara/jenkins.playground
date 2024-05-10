#!/bin/sh

# This script clears all the stuff the gwt compilation generates under your eclipse GWT project during when performing a full gwt compilation.
# 1. parameter: the path to your module eclipse project

echo "removing the gwt stuff under the folder '$1'..."
echo "The application name is '$2'..."

cd $1
rm gwt-unitCache/*  || /bin/true
rm -r WebContent/$2/*  || /bin/true
rm -r WebContent/WEB-INF/deploy/$2  || /bin/true

echo "All relevant files have been removed"
