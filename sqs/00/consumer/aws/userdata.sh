#!/bin/bash

# Install Java 21
sudo yum update -y \
    && sudo yum install openjdk-21-jdk -y

# Download JAR package from S3
aws s3 cp s3://queue-src00/consumer/app.jar /root/app.jar

# Start the app
java -Dsqs.queue.name=basic-q -Dsqs.region=us-east-1 -jar /root/app.jar