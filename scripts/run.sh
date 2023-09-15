#!/usr/bin/env bash

JAR_FILE=$(ls build/libs/*.jar*)

APP_LOG="application.log"
ERROR_LOG="error.log"
DEPLOY_LOG="deploy.log"

TIME_NOW=$(date)

chmod 755 $JAR_FILE

sudo cp -r ../static src/main/resources
sudo chmod -R 777 src/main/resources/static

echo "[ $TIME_NOW ] Run java application $JAR_FILE" >>$DEPLOY_LOG
nohup java -jar -Dspring.profiles.active=prod $JAR_FILE &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "[ $TIME_NOW ] Application running in PID $CURRENT_PID" >>$DEPLOY_LOG
