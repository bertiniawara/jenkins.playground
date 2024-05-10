#!/bin/sh

moduleName="cm.lao.$1.web"
version=$2

rm -rf $moduleName
mkdir -p $moduleName/downloads
cp ~/git-repositories/cm.lao.common.build/deployment/docker/microservice/frontend/Dockerfile $moduleName

#download the war file
productBaseDir=$(pwd)
cd $moduleName/downloads
wget --no-check-certificate https://ivy.lao-sarl.cm/repository/lao/$moduleName/zips/$moduleName-$version.zip

zipFile="$(pwd)/$moduleName-$version.zip"
cd $productBaseDir

# configure the installs directory
mkdir $moduleName/web
cd $moduleName/web
unzip $zipFile
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
