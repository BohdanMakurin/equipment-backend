pipeline {
    agent any

    stages {
        // Этап клонирования репозитория из Git
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/perdoshmit/equipment-backend.git'
            }
        }

        // Этап сборки проекта с помощью Maven
        stage('Build') {
            steps {
                echo 'Building the project...'
                bat 'F:\\Programs\\apache-maven-3.9.9\\bin\\mvn clean compile'
            }
        }

        // Этап тестирования
        stage('Test') {
            steps {
                echo 'Running tests...'
                bat 'F:\\Programs\\apache-maven-3.9.9\\bin\\mvn test'
            }
            post {
                always {
                    // Публикуем результаты тестов, найденные в папке target/surefire-reports
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        // Этап генерации JavaDoc
        stage('Generate JavaDoc') {
            steps {
                echo 'Generating JavaDoc...'
                bat 'F:\\Programs\\apache-maven-3.9.9\\bin\\mvn javadoc:javadoc'
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }

        success {
            echo 'Build succeeded!'
        }

        failure {
            echo 'Build failed.'
        }
    }
}
