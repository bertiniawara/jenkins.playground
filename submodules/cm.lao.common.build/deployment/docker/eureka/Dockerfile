FROM tomcat:9.0-jdk11

LABEL maintainer="LAO"

ADD https://ivy.lao-sarl.cm/repository/javax.xml.bind/jaxb-api/jars/jaxb-api-2.3.1.jar /usr/local/tomcat/lib
ADD https://ivy.lao-sarl.cm/repository/com.sun.xml.bind/jaxb-core/jars/jaxb-core-2.3.0.1.jar /usr/local/tomcat/lib
ADD https://ivy.lao-sarl.cm/repository/com.sun.xml.bind/jaxb-impl/jars/jaxb-impl-2.3.1.jar /usr/local/tomcat/lib
ADD https://ivy.lao-sarl.cm/repository/javax.activation/javax.activation-api/jars/javax.activation-api-1.2.0.jar /usr/local/tomcat/lib

COPY ./war /usr/local/tomcat/webapps/ROOT

EXPOSE 8080
