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

aws s3 mb s3://queue-src00

mvn package -f producer/pom.xml
aws s3 cp ./producer/target/producer-0.0.1-SNAPSHOT.jar s3://queue-src00/producer/app.jar

mvn package -f consumer/pom.xml
aws s3 cp ./consumer/target/consumer-0.0.1-SNAPSHOT.jar s3://queue-src00/consumer/app.jar


aws sqs get-queue-url --queue-name basic-q

aws sqs get-queue-attributes --queue-url https://sqs.us-east-1.amazonaws.com/450346197415/basic-q --attribute-names QueueArn

aws iam create-role --role-name sqs00consumer --assume-role-policy-document file://assume-role-policy.json
aws iam create-policy --policy-name sqs00consumer-policy --policy-document file://consumer/aws/ec2rolepolicy.json
aws iam attach-role-policy --role-name sqs00consumer --policy-arn arn:aws:iam::450346197415:policy/sqs00consumer-policy

aws iam create-role --role-name sqs00producer --assume-role-policy-document file://assume-role-policy.json





