pipeline {
    agent any

    stages {

// Stage - 1 to checkout the code from github
//
        stage("Clone Github") {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/pal40/tf-server']])
            }
        }
// Stage - 2 to get AWS Credentials
//
        stage("AWS Cred") {
            steps {
                    withCredentials([string(credentialsId: 'aws-jenkins', variable: 'secret')]) {
                        script {
                        def creds = readJSON text: secret
                        env.AWS_ACCESS_KEY_ID = creds['accessKeyId']
                        env.AWS_SECRET_ACCESS_KEY = creds['secretAccessKey']
                        env.AWS_REGION = 'ap-southeast-1'
                                }   
        sh "aws sts get-caller-identity"
    }
            }
        }

// Stage - 3 to init terraform
//
        stage("Terrform Init") {
            steps {
                sh ("terraform init");
            }
        }

// Stage - 4 to perform the terraform action
//
        stage("Terrform Action") {
            steps {
                echo "Terraform action from the Parameter is -->> ${action}"
                sh ("terraform ${action}");
            }
        }

// Stage - 5 to add checkov and scan the terraform code
//
        stage("Checkov Scan") {
            steps {
                sh ("/usr/local/bin/checkov -d . ");
				echo "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
				echo "Compact version below: "
				echo "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
				sh ("/usr/local/bin/checkov -d . --compact");
            }
        }
    }
}