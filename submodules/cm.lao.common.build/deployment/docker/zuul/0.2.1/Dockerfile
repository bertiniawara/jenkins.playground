FROM openjdk:11

LABEL maintainer="LAO"

RUN whereis java
RUN java --version

COPY  ./zuul /opt/zuul

RUN mkdir -p /var/lib/zuul
ENV CM_BAO_ZUUL_OPTS="-Dzuul.home=/var/lib/zuul"

EXPOSE 7001

COPY ./start.sh /start.sh
RUN ["chmod", "+x", "/start.sh"]
ENTRYPOINT ["sh", "/start.sh"]
