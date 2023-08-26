pipeline {
    agent any

    stages {

// Stage - 1 to checkout the code from github
//
        stage('Hello') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/pal40/tf-server']])
            }
        }

// Stage - 2 to init terraform
//
        stage("Terrform init") {
            steps {
                sh ("terraform init");
            }
        }

// Stage - 3 to perform the terraform action
//
        stage("Terrform Action") {
            steps {
                echo "Terraform action from the Parameter is -->> ${action}"
                sh ("terraform ${action}");
            }
        }

// Stage - 4 to add checkov and print version
//
        stage("Verify checkov version") {
            steps {
                sh ("/usr/local/bin/checkov --version");
            }
        }

// Stage - 5 to add checkov and scan the terraform code
//
        stage("Check the code in current working directory") {
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