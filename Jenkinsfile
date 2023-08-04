pipeline { 
    agent any  
    stages {  
         stage('git') {
            steps {
                url: 'https://github.com/ikinurmaulana/studi-devsecops-repo.git'
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
