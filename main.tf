provider "aws" {
    region =  "ap-southeast-1"  # Asia Pacific (Singapore)
}

terraform {
    backend "s3" {
        bucket = "tf-server-backend-26aug2023"
        key = "main"
        region = "ap-southeast-1"
        dynamodb_table = "tf-server-backend-dynamodb"
        profile = "default"
    }
}