# CRUD Files (No update though)
aws s3api create-bucket --bucket awspractice-name00

aws s3 cp pwd.txt s3://awspractice-name00/key.txt

aws s3 ls awspractice-name00

aws s3 rm s3://awspractice-name00/key.txt


# IAM principle (user/group/role) can access S3 object when
# The user IAM permissions ALLOW it OR the resource policy ALLOWS it.
# AND there is no explicit DENY

# Bucket policy - standard iam policy document
# resources - bucket and objects "arn:aws:s3::bucketname/*"
# principal - user or account to apply policy to





