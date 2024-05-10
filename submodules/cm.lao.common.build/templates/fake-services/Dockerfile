FROM node:14.16

LABEL maintainer="LAO"

COPY . opt/app/

RUN cd /opt/app \
    && npm install \
    && echo "{ \"watch\": false }" > nodemon.json

EXPOSE 28960

ENTRYPOINT cd /opt/app && npm run start:dev
