#!/bin/sh
# Create bucket, allow public access
# create policy, role, instance-profile, attache them
# run ec2 instance, attach intance profile

aws s3api create-bucket --bucket awspractice-name01

aws s3api delete-public-access-block --bucket awspractice-name01

aws iam create-policy --policy-name p1 --policy-document file://access-policy.json

aws iam create-role \
    --role-name s3-01 \
    --assume-role-policy-document file://trust-policy.json

aws iam attach-role-policy --policy-arn arn:aws:iam::450346197415:policy/p1 --role-name s3-01

aws iam create-instance-profile --instance-profile-name s3-01-inst-prof

aws iam add-role-to-instance-profile \
          --instance-profile-name s3-01-inst-prof \
          --role-name s3-01

aws ec2 run-instances \
    --image-id ami-07caf09b362be10b8 \
    --instance-type t2.micro \
    --key-name test \
    --security-group-ids sg-020d0b6bf5b898fd9 sg-074c4771bb2362c9c \
    --user-data file://userdata.sh \
    --count 1 > 00_runinstancesout.json

aws ec2 associate-iam-instance-profile --instance-id i-07d99deeaaea8eac8 --iam-instance-profile Name=s3-01-inst-prof