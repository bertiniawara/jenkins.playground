#!/bin/sh 

# stop the server where to deploy
stop_tomcat() {
	sh $TOMCAT_HOME/bin/shutdown.sh
	sleep 30

	touch "$clearLogFiles.file"
	if [ -e 'true.file' ];
		then
    		touch $TOMCAT_HOME/logs/dummyByJenkins.log
    		rm $TOMCAT_HOME/logs/*
	fi
}

# start the server again
start_tomcat() {
	sh $TOMCAT_HOME/bin/startup.sh &
	sleep 30
}

restart_tomcat() {
	stop_tomcat
	start_tomcat
}


### Finally the input handling.

case "$1" in
  start)
        start_tomcat
        ;;
  stop)
        stop_tomcat
        ;;
  restart)
        restart_tomcat
        ;;
  *)
        echo "Usage:  {start|stop|restart}"
        exit 1
        ;;
esac

exit

