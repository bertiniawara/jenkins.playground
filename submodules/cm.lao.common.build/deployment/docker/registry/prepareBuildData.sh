#!/bin/sh

# ssl keys
rm -rf certs
sudo cp -rL /etc/letsencrypt/live/LAO /certs
sudo chown -R appdir certs
