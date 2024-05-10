#!/bin/sh

moduleName="cm.bao.zuul"
version=$1

rm -rf $moduleName

mkdir -p $moduleName/downloads
cp ~/git-repositories/cm.lao.common.build/deployment/docker/zuul/$version/Dockerfile $moduleName
cp ~/git-repositories/cm.lao.common.build/deployment/docker/zuul/$version/application.properties $moduleName
cp ~/git-repositories/cm.lao.common.build/deployment/docker/zuul/$version/start.sh $moduleName

#download
productBaseDir=$(pwd)
cd $moduleName/downloads
wget --no-check-certificate https://ivy.lao-sarl.cm/repository/lao/cm.lao.zuul/zips/cm.lao.zuul-$version.zip
zipFile="$(pwd)/cm.lao.zuul-$version.zip"
cd $productBaseDir/$moduleName
unzip $zipFile
mv cm.bao.zuul_bin-$version zuul
zip -r zuul/lib/cm.bao.zuul-0.2.1.jar application.properties

# build the image
imageNamePlusTag=$moduleName:$version
docker image build -t $imageNamePlusTag .

# push the image to the private registry
registryHost="127.0.0.1"

docker image tag $imageNamePlusTag $registryHost:5000/$imageNamePlusTag
docker image push $registryHost:5000/$imageNamePlusTag

# cleaning
docker image rm $registryHost:5000/$imageNamePlusTag
