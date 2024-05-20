#!/bin/bash

aws ec2 create-security-group --group-name PostgresIngress --description "UG for Posgress 5432 port"

aws ec2 authorize-security-group-ingress     --group-id sg-02ece95d0c10c004f     --protocol tcp     --port 5432     --cidr 0.0.0.0/0

aws rds create-db-instance --db-name testdb \
    --db-instance-identifier mydbinstance \
    --db-instance-class db.t3.micro \
    --engine postgres \
    --master-username rootuser \
    --master-user-password myrootpwd \
    --allocated-storage 20 \
    --vpc-security-group-ids sg-02ece95d0c10c004f \
    --availability-zone us-east-1a \
    --engine-version 13

