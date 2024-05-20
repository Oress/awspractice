#!/bin/bash

# Plan:
# 0. create instances
# 1. create load balancer
# 2. create target group for instances
# 3. register instances with the target group
# 4. create listener(inbound rule) for ALB
# 5. PROFIT

aws ec2 run-instances \
    --image-id ami-07caf09b362be10b8 \
    --instance-type t2.small \
    --key-name test \
    --security-group-ids sg-0f49bf7f2faf92eb8 sg-020d0b6bf5b898fd9 sg-074c4771bb2362c9c > 00_runinstancesout.json


aws ec2 create-security-group --group-name FactorioIngress --description "This sg allows inbound traffic for port 34197" > 01_createsg.json


sg-0a43eca0ca37385ba

aws ec2 authorize-security-group-ingress \
    --group-id sg-0a43eca0ca37385ba \
    --protocol udp \
    --port 34197 \
    --cidr 0.0.0.0/0



aws ec2 modify-instance-attribute --instance-id i-0fb74808184fee8f6 --groups sg-0f49bf7f2faf92eb8 sg-020d0b6bf5b898fd9 sg-074c4771bb2362c9c sg-0a43eca0ca37385ba