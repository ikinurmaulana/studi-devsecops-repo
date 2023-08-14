pipeline {
    agent any
    stages {
        stage('Sonarqube') {
            environment {
              SONAR_SCANNER = tool('sonar-scanner')
            }
          steps {
            script {
                withSonarQubeEnv(credentialsId: 'sonarqube-secret', installationName: 'sonarqube-server') {
                sh '''${SONAR_SCANNER}/bin/sonar-scanner \
                -Dsonar.projectKey=PR-Studi-DevSecOps \
                -Dsonar.sources=. \
                -Dsonar.java.jdkHome=/usr/lib/jvm/java-1.17.0-openjdk-amd64 \
                -Dsonar.java.binaries=**/* '''
                }
            }
          }
        }
        stage("Semgrep") {
          stage{
            sh ''' echo "test 2"
            semgrep --version
            semgrep ci'''

          }
        }
    }
