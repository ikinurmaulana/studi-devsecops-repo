pipeline { 
    agent any  
    stages {  
         stage("Code Checkout from GitLab") { 
            environment {   
              GITLAB_TOKEN = credentials('5ee3085d-0fa7-4219-886c-4fa057ed8bfe')  
            } 
            steps {
              git branch: 'master',  
              url: "https://github.com/ikinurmaulana/studi-devsecops-repo"
            } 
         } 
         stage('Sonar') {
            environment {
                SCANNER_HOME = tool 'sonar-scanner'
            }
            steps {
            withSonarQubeEnv(credentialsId: 'SonarQube-token', installationName: 'Sonarqube-test') {
            sh '''$SCANNER_HOME/bin/sonar-scanner \
            -Dsonar.projectKey=project-jenkins \
            -Dsonar.projectName=project-jekins \
            -Dsonar.java.jdkHome=/usr/lib/jvm/java-1.17.0-openjdk-amd64 \
            -Dsonar.java.binaries=**/* '''
            }
        }
    }
}
}
