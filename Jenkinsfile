pipeline {
    agent any

    tools {
        maven 'Maven 3.9.9'
    }
    stages {

        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/perdoshmit/equipment-backend.git'
            }
        }


        stage('Build') {
            steps {
                echo 'Building the project...'
                bat 'mvn clean package'
            }
        }


        stage('Test') {
            steps {
                echo 'Running tests...'
                bat 'mvn test'
            }
            post {
                always {

                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }


        stage('Generate JavaDoc') {
            steps {
                echo 'Generating JavaDoc...'
                bat 'mvn javadoc:javadoc'
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }

        success {
            echo 'Build succeeded!'

                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true

                archiveArtifacts artifacts: '**/target/site/apidocs/**', allowEmptyArchive: true

                publishHTML([
                    reportDir: 'target/site/apidocs',
                    reportFiles: 'index.html',
                    reportName: 'JavaDoc',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])
        }

        failure {
            echo 'Build failed.'
        }
    }
}
