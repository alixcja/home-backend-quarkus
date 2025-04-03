pipeline {
    agent any
        environment {
            TAG = "{$BUILD_NUMBER}"
    }
    stages {
        stage('mvn build') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-21'
                    reuseNode true
                    }
                }
            steps {
                sh "mvn -B -DskipTests clean package"
            }
        }
        stage('docker build') {
            steps {
                sh "docker build -f src/main/docker/Dockerfile.jvm -t alixcja/home-backend:dev-${TAG} . "
            }
        }
        stage('docker push') {
            steps {
                sh "docker push alixcja/home-backend:dev-${TAG}"
            }
        }
    }
}