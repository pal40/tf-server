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