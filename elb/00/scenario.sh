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
    --instance-type t2.micro \
    --key-name test \
    --security-group-ids sg-0f49bf7f2faf92eb8 sg-020d0b6bf5b898fd9 sg-074c4771bb2362c9c \
    --user-data file://userdata \
    --count 2 > 00_runinstancesout.json

# This var contains list of instance ids id1 id2 ...
instances=$(jq '[.Instances[].InstanceId] | join(", ")' 00_runinstancesout.json)


aws elbv2 create-load-balancer \
    --name elb-test-00 \
    --subnets subnet-0f9aee65694e1cf4f subnet-090d595e9aa295e4e \
    --security-groups sg-0f49bf7f2faf92eb8 sg-074c4771bb2362c9c \
    --scheme internet-facing \
    --type application > 01_createelb.json

# This var contains list of instance ids id1 id2 ...
instances=$(jq '[.Instances[].InstanceId] | join(", ")' 00_runinstancesout.json)


aws elbv2 create-target-group \
    --name tg-00 \
    --vpc-id vpc-0f8c4caa54f431f32 \
    --protocol HTTP \
    --port 80 \
    --health-check-protocol HTTP \
    --health-check-port 80 \
    --health-check-path / \
    --target-type instance > 02_createtg.json


# Example
# aws elbv2 register-targets \
#     --target-group-arn arn:aws:elasticloadbalancing:us-east-1:450346197415:targetgroup/tg-00/b2b577e0aff4cd17 \
#     --targets Id=i-01094ee2904960316 Id=i-02e5873855b3b42f8 > 03_registertrgs.json
tg_arn=$(jq '.TargetGroups[0].TargetGroupArn' 02_createtg.json)
targets=$(jq '["Id=" + .Instances[].InstanceId] | join(" ")' 00_runinstancesout.json)
aws elbv2 register-targets \
    --target-group-arn $tg_arn \
    --targets $targets \


# Example
# aws elbv2 create-listener \
#     --load-balancer-arn arn:aws:elasticloadbalancing:us-east-1:450346197415:loadbalancer/app/elb-test-00/0bce33c22409bcad \
#     --protocol HTTP \
#     --default-actions Type=forward,TargetGroupArn=arn:aws:elasticloadbalancing:us-east-1:450346197415:targetgroup/tg-00/b2b577e0aff4cd17 \
#     --port 80 > 04_cratelist.json
actions="Type=forward,TargetGroupArn=$tg_arn"
lb_arn=$(jq '.LoadBalancers[0].LoadBalancerArn' 01_createelb.json)
aws elbv2 create-listener \
    --load-balancer-arn $lb_arn \
    --default-actions $actions\
    --protocol HTTP
    --port 80 > 04_cratelist.json



