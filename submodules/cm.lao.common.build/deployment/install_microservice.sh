#!/bin/sh

moduleName="cm.lao.$1"
version=$2
tomcatPort1=$3
tomcatPort2=$4
appDb=$5
appDbUser=$6
appDbUserPw=$7

#download the war file
productBaseDir=$(pwd)
mkdir -p ~/downloads
cd ~/downloads
wget --no-check-certificate http://ivy.lao-sarl.cm/repository/lao/cm.lao.$1.ui.web/wars/cm.lao.$1.ui.web-$version.war
#wget http://ivy.lao-sarl.cm/repository/lao/cm.lao.$1.ui.gwt/wars/cm.lao.$1.ui.gwt-$version.war
warFile="$(pwd)/cm.lao.$1.ui.web-$version.war"
#warFile="$(pwd)/cm.lao.$1.ui.gwt-$version.war"
cd $productBaseDir

# create the folder structure
tar -xf microservice_template.tar.gz
mv microservice_template $moduleName

# set tomcat ports
sed "s/tomcatPort1/$tomcatPort1/"  $moduleName/servers/tomcat/current/conf/server.xml > $moduleName/servers/tomcat/current/conf/server1.xml
sed "s/tomcatPort2/$tomcatPort2/"  $moduleName/servers/tomcat/current/conf/server1.xml > $moduleName/servers/tomcat/current/conf/server.xml
rm $moduleName/servers/tomcat/current/conf/server1.xml

# set database config
sed "s/XXX/$appDb/"  $moduleName/home/current/dbconfig.xml > $moduleName/home/current/dbconfig1.xml
sed "s/YYY/$appDbUser/"  $moduleName/home/current/dbconfig1.xml > $moduleName/home/current/dbconfig2.xml
sed "s/ZZZ/$appDbUserPw/"  $moduleName/home/current/dbconfig2.xml > $moduleName/home/current/dbconfig.xml
rm $moduleName/home/current/dbconfig1.xml
rm $moduleName/home/current/dbconfig2.xml

# configure the home directory
mkdir -p $moduleName/home/$version
cp -rf $moduleName/home/current/* $moduleName/home/$version/
rm $moduleName/home/current
cd $moduleName/home/
ln -s $version current
rm -rf current/logs/*
cd $productBaseDir

# configure the installs directory
mkdir -p $moduleName/installs/$version
cd $moduleName/installs/$version
unzip $warFile
cd ..
rm current
ln -s $version current
rm -rf current/WEB-INF/classes/eureka-client-test.properties
cd $productBaseDir
