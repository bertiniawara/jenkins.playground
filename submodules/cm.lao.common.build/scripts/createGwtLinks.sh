#!/bin/bash

cd ~
mkdir -p .gwt

ln -s ~/.ivy2/cache/com.google.gwt/gwt-user/jars/gwt-user-$1.jar ~/.gwt/gwt-user.jar
ln -s ~/.ivy2/cache/com.google.gwt/gwt-dev/jars/gwt-dev-$1.jar ~/.gwt/gwt-dev.jar
ln -s ~/.ivy2/cache/com.google.gwt/gwt-servlet/jars/gwt-servlet-$1.jar ~/.gwt/gwt-servlet.jar
ln -s ~/.ivy2/cache/com.google.gwt/gwt-servlet/jars/gwt-codeserver-$1.jar ~/.gwt/gwt-codeserver.jar

echo "The following links have been created: "
ls -al ~/.gwt

