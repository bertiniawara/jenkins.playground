#!/bin/sh

moduleName="cm.lao.$1.web"
version=$2
webserver_group=$3

#download the war file
productBaseDir=$(pwd)
mkdir -p ~/downloads
cd ~/downloads
wget http://ivy.lao-sarl.cm/repository/lao/$moduleName/zips/$moduleName-$version.zip
zipFile="$(pwd)/$moduleName-$version.zip"
cd $productBaseDir

# configure the home directory
rm -rf $moduleName/$version
mkdir -p $moduleName/$version
rm -f $moduleName/current
cd $moduleName
pathToTheNewInstallation=$(pwd)
ln -s $version current
cd current
unzip $zipFile

if [ -z "$webserver_group" ]
  then
    echo "No specific group passed as webserver group runner. 'www-data' will be used"
    webserver_group="www-data"
fi

echo "group to be used is '$webserver_group'"
cd ..
sudo chgrp -R $webserver_group $pathToTheNewInstallation

cd $productBaseDir
