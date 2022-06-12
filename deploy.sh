#!/bin/bash

PROJECT_NAME=${PWD##*/}
DOCKER_IMAGE_NAME="michalkolos/${PROJECT_NAME}-server"
USERNAME="pi"
HOSTNAME="192.168.0.11"
REMOTE_REPO_DIR="~/repos"
REMOTE_PATH="/home/pi/.sdkman/candidates/java/current/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/local/games:/usr/games:/snap/bin"


echo 'FROM openjdk:17.0.2-jdk' > Dockerfile
echo 'ARG JAR_FILE=target/*.jar' >> Dockerfile
echo 'COPY ${JAR_FILE}' ${PROJECT_NAME}'.jar' >> Dockerfile
echo 'ENTRYPOINT ["java","-jar","'/${PROJECT_NAME}'.jar", "--spring.profiles.active=prod"]' >> Dockerfile



REMOTE_SCRIPT="export PATH=$REMOTE_PATH;java --version;echo $PATH;cd ${REMOTE_REPO_DIR}/${PROJECT_NAME};./gradlew build; docker build --build-arg JAR_FILE=build/libs/\*SNAPSHOT.jar -t ${DOCKER_IMAGE_NAME} .; docker-compose up -d"


printf '%s\n' ${PROJECT_NAME}

rsync -aP --delete . ${USERNAME}@${HOSTNAME}:${REMOTE_REPO_DIR}/"${PROJECT_NAME}"
ssh -l ${USERNAME} ${HOSTNAME} "${REMOTE_SCRIPT}"
 
 
