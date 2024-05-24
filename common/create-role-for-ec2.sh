#!/bin/bash

# 1 variable - role name
# 2 variable - reference to the assume-role-policy-document  file://...
# 3 variable - reference to the policy-document  file://...

roleName="$1"
policyName="$roleName-pol"
instanceProfileName="$roleName-ipn"

if [[ -z "$1" ]]; then
    echo "Role name is required!"
    exit 1
fi

if [[ -z "$2" ]]; then
    echo "Assume role policy document is required!"
    exit 1
fi

if [[ -z "$3" ]]; then
    echo "Policy document is required!"
    exit 1
fi

aws iam create-role --role-name $roleName --assume-role-policy-document $2
policyjson = $(aws iam create-policy --policy-name $policyName --policy-document $3)

jq '.Policy.Arn' policyjson

aws iam attach-role-policy --role-name $roleName --policy-arn arn:aws:iam::450346197415:policy/sqs00consumer-policy
aws iam create-instance-profile --instance-profile-name $instanceProfileName
aws iam add-role-to-instance-profile --instance-profile-name $instanceProfileName --role-name $roleName
