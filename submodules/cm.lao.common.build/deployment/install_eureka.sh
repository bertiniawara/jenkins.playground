#!/bin/sh

moduleName="eureka"
version=$1
tomcatPort1=$2
tomcatPort2=$3

#download the war file
productBaseDir=$(pwd)
cd ~/downloads
wget https://ivy.lao-sarl.cm/repository/com.netflix.eureka/eureka-server/wars/eureka-server-$version.war
warFile="$(pwd)/eureka-server-$version.war"
cd $productBaseDir

# create the folder structure
tar -xf microservice_template.tar.gz
mv microservice_template $moduleName

# set tomcat ports
sed "s/tomcatPort1/$tomcatPort1/"  $moduleName/servers/tomcat/current/conf/server.xml > $moduleName/servers/tomcat/current/conf/server1.xml
sed "s/tomcatPort2/$tomcatPort2/"  $moduleName/servers/tomcat/current/conf/server1.xml > $moduleName/servers/tomcat/current/conf/server.xml
rm $moduleName/servers/tomcat/current/conf/server1.xml

# download the jaxb related libraries
cd $moduleName/servers/tomcat/current/lib
wget https://ivy.lao-sarl.cm/repository/javax.xml.bind/jaxb-api/jars/jaxb-api-2.3.1.jar
wget https://ivy.lao-sarl.cm/repository/com.sun.xml.bind/jaxb-core/jars/jaxb-core-2.3.0.1.jar
wget https://ivy.lao-sarl.cm/repository/com.sun.xml.bind/jaxb-impl/jars/jaxb-impl-2.3.1.jar
wget https://ivy.lao-sarl.cm/repository/javax.activation/javax.activation-api/jars/javax.activation-api-1.2.0.jar
cd $productBaseDir

# configure the home directory
rm -rf $moduleName/home

# configure the installs directory
mkdir $moduleName/installs/$version
cd $moduleName/installs/$version
unzip $warFile 
cd ..
rm current
ln -s $version current
cd $productBaseDir

sh $moduleName/servers/tomcat/current/bin/startup.sh

