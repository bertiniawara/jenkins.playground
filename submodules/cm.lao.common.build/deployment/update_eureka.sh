#!/bin/sh

moduleName="eureka"
version=$1

#download the war file
productBaseDir=$(pwd)
cd ~/downloads
wget https://ivy.lao-sarl.cm/repository/com.netflix.eureka/eureka-server/wars/eureka-server-$version.war
warFile="$(pwd)/eureka-server-$version.war"
cd $productBaseDir

# stop the servers
sh $moduleName/servers/tomcat/current/bin/shutdown.sh
echo 'tomcat stopping...'
sleep 30
echo 'tomcat stopped...'

# configure the installs directory
mkdir $moduleName/installs/$version
cd $moduleName/installs/$version
unzip $warFile 
cd ..
cp current/WEB-INF/classes/eureka-client.properties $version/WEB-INF/classes
rm current
ln -s $version current
cd $productBaseDir

# start server
echo 'starting tomcat...'
sh $moduleName/servers/tomcat/current/bin/startup.sh

# open the server log
sleep 2
tail -fn200 $moduleName/servers/tomcat/current/logs/catalina.out

