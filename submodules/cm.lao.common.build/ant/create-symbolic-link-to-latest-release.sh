#!/bin/bash

ivyRepositoryBaseDirectory=/home/ivymanager/ivysite/repository
pathToTheFileToLink=$ivyRepositoryBaseDirectory/$1
nameOfTheLink=$2

fullPathToTheSymbolicLink=$(dirname $pathToTheFileToLink)/$nameOfTheLink

rm $fullPathToTheSymbolicLink
ln -s $pathToTheFileToLink $fullPathToTheSymbolicLink

