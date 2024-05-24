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
aws iam create-instance-profile --instance-profile-name sqs00consumer-ipn
aws iam add-role-to-instance-profile --instance-profile-name sqs00consumer-ipn --role-name sqs00consumer


aws iam create-role --role-name sqs00producer --assume-role-policy-document file://assume-role-policy.json
aws iam create-policy --policy-name sqs00producer-policy --policy-document file://producer/aws/ec2rolepolicy.json
aws iam attach-role-policy --role-name sqs00producer --policy-arn arn:aws:iam::450346197415:policy/sqs00producer-policy
aws iam create-instance-profile --instance-profile-name sqs00producer-ipn
aws iam add-role-to-instance-profile --instance-profile-name sqs00producer-ipn --role-name sqs00producer

aws ec2 run-instances --image-id ami-07caf09b362be10b8\
    --iam-instance-profile Name=sqs00consumer-ipn \
    --instance-type t2.micro \
    --key-name test \
    --user-data file://consumer/aws/userdata.sh \
    --security-group-ids sg-0f49bf7f2faf92eb8 sg-020d0b6bf5b898fd9 sg-074c4771bb2362c9c


aws ec2 run-instances --image-id ami-07caf09b362be10b8\
    --iam-instance-profile Name=sqs00producer-ipn \
    --instance-type t2.micro \
    --key-name test \
    --user-data file://producer/aws/userdata.sh \
    --security-group-ids sg-0f49bf7f2faf92eb8 sg-020d0b6bf5b898fd9 sg-074c4771bb2362c9c






