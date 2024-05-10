#!/bin/sh

moduleName="cm.lao.$1"
version=$2

#download the war file
productBaseDir=$(pwd)
mkdir -p ~/downloads
cd ~/downloads
wget --no-check-certificate http://ivy.lao-sarl.cm/repository/lao/cm.lao.$1.ui.web/wars/cm.lao.$1.ui.web-$version.war
#wget http://ivy.lao-sarl.cm/repository/lao/cm.lao.$1.ui.gwt/wars/cm.lao.$1.ui.gwt-$version.war
warFile="$(pwd)/cm.lao.$1.ui.web-$version.war"
#warFile="$(pwd)/cm.lao.$1.ui.gwt-$version.war"
cd $productBaseDir

# stop the servers
sh $moduleName/servers/tomcat/current/bin/shutdown.sh
echo 'tomcat stopping...'
sleep 30
echo 'tomcat stopped...'

# configure the home directory
mkdir $moduleName/home/$version
cp -rf $moduleName/home/current/* $moduleName/home/$version/
rm $moduleName/home/current
cd $moduleName/home/
ln -s $version current
rm -rf current/logs/*
cd $productBaseDir

# configure the installs directory
mkdir $moduleName/installs/$version
cd $moduleName/installs/$version
unzip $warFile 
cd ..
cp current/WEB-INF/web.xml $version/WEB-INF/
cp current/WEB-INF/classes/eureka-client.properties $version/WEB-INF/classes
rm current
ln -s $version current
rm -rf current/WEB-INF/classes/eureka-client-test.properties

cd $productBaseDir

# start server
echo 'starting tomcat...'
sh $moduleName/servers/tomcat/current/bin/startup.sh

# open the server log
sleep 2
tail -fn200 $moduleName/servers/tomcat/current/logs/catalina.out
 
