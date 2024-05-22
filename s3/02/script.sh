#!/bin/bash

# create a simple static website on s3
# enable versioning and update index.html

aws s3 mb s3://awspractice-name02

aws s3api delete-public-access-block --bucket awspractice-name02

aws s3 cp index.html s3://awspractice-name02/index.html

aws s3api put-bucket-policy --bucket awspractice-name02 --policy file://bucket-policy.json

aws s3api put-bucket-website --bucket awspractice-name02 --website-configuration file://website.json

aws s3api put-bucket-versioning --bucket awspractice-name02 --versioning-configuration Status=Enabled

aws s3 cp indexv2.html s3://awspractice-name02/index.html