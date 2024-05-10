#!/bin/sh

moduleName="cm.lao.$1"
version=$2
appDb=$3
appDbUser=$4
appDbUserPw=$5

rm -rf $moduleName
mkdir -p $moduleName/downloads
cp ~/git-repositories/cm.lao.common.build/deployment/docker/microservice/backend/Dockerfile $moduleName
cp ~/git-repositories/cm.lao.common.build/deployment/docker/microservice/backend/dbconfig.xml $moduleName
sed "s/eurekaName/$moduleName/" ~/git-repositories/cm.lao.common.build/deployment/docker/microservice/backend/eureka-client.properties > $moduleName/eureka-client.properties

# set database config
sed "s/XXX/$appDb/" ~/git-repositories/cm.lao.common.build/deployment/docker/microservice/backend/dbconfig.xml > $moduleName/dbconfig1.xml
sed "s/YYY/$appDbUser/" $moduleName/dbconfig1.xml > $moduleName/dbconfig2.xml
sed "s/ZZZ/$appDbUserPw/" $moduleName/dbconfig2.xml > $moduleName/dbconfig.xml
rm $moduleName/dbconfig1.xml
rm $moduleName/dbconfig2.xml

#download the war file
productBaseDir=$(pwd)
cd $moduleName/downloads
wget --no-check-certificate https://ivy.lao-sarl.cm/repository/lao/$moduleName.ui.web/wars/$moduleName.ui.web-$version.war
warFile="$(pwd)/$moduleName.ui.web-$version.war"
cd $productBaseDir

# configure the installs directory
mkdir $moduleName/war
cd $moduleName/war
unzip $warFile
cd $productBaseDir/$moduleName

# build the image
imageNamePlusTag=$moduleName:$version
docker image build -t $imageNamePlusTag .

# push the image to the private registry
registryHost="127.0.0.1"

docker image tag $imageNamePlusTag $registryHost:5000/$imageNamePlusTag
docker image push $registryHost:5000/$imageNamePlusTag

# cleaning
docker image rm $registryHost:5000/$imageNamePlusTag
