#!/bin/sh

moduleName="cm.lao.zuul"
product=$1
version=$2
# a third argument with the value 'first' should be specified if zuul is specified for the first time 

#download the war file
productBaseDir=$(pwd)
cd ~/downloads
wget https://ivy.lao-sarl.cm/repository/lao/cm.lao.zuul/zips/cm.lao.zuul-$version.zip
zipFile="$(pwd)/cm.lao.zuul-$version.zip"
cd $productBaseDir

if [ "$3" = "first" ]
then
  # create the folder structure
  tar -xf zuul_template.tar.gz
fi

# configure the home directory
mkdir $moduleName/home/$version
cp -rf $moduleName/home/current/* $moduleName/home/$version/
rm $moduleName/home/current
cd $moduleName/home/
ln -s $version current
mkdir -p current/logs
rm -rf current/logs/*
cd $productBaseDir

# configure the installs directory
cd $moduleName/installs/
unzip $zipFile 
mv cm.lao.zuul_bin-$version $version

if [ "$3" != "first" ]
then
  # using the application.properties file from the previously used version
  echo "Using the application.properties file form the previously used version..."
  echo "unpacking the cm.lao.zuul.jar from the old version"
  rm -rf tmp
  unzip current/lib/cm.lao.zuul-*.jar -d tmp
  cd tmp
  zip -r ../$version/lib/cm.lao.zuul-$version.jar application.properties 
  cd ..
  echo "appliation.properties file copied from old to new version..."
  rm -rf tmp
  echo "temporary directory cleared..."
fi

rm current
ln -s $version current
cd $productBaseDir

