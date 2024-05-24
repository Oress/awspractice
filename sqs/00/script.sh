#!/bin/bash

# Use case 00. Basic Scenario producer --> queue --> consumer
# X 1. create queue
# X 2. write a producer that sends messages to the queue
# 3. write a consumer that reads messages from the queue
# 4. build them and upload to the s3
# 5. run a ec2 instances with the consumer and producer. Give them the permission to read the package from s3. 
# 6. Write a userdata script that downloads the packages and updates yum, installs java and runs the apps.

aws sqs create-queue --queue-name basic-q

mvn package -f producer/pom.xml

aws s3 mb queue-src

mvn build -f producer/pom.xml
aws s3 cp ./producer/target/producer-0.0.1-SNAPSHOT.jar s3://queue-src/producer/app.jar




