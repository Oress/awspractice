#!/bin/bash

# Install Java 21
sudo yum update -y \
    && sudo yum install java-21-amazon-corretto -y

# Download JAR package from S3
aws s3 cp s3://queue-src00/producer/app.jar /root/app.jar

# Start the app
java -Dsqs.queue.name=basic-q -Dsqs.region=us-east-1 -jar /root/app.jar