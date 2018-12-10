#!/usr/bin/env groovy

pipeline {
    agent any

    triggers {
        pollSCM('*/15 * * * *')
    }

    options { disableConcurrentBuilds() }

    stages {
        stage('Permissions') {
            steps {
                sh 'chmod 775 *'
            }
        }
		
		stage('Cleanup') {
            steps {
                sh './gradlew --no-daemon clean'
            }
        }

        stage('Check Style, FindBugs, PMD') {
            steps {
                sh './gradlew --no-daemon checkstyleMain checkstyleTest findbugsMain findbugsTest pmdMain pmdTest cpdCheck'
            }
			post {
				always {
					step([
						$class         : 'FindBugsPublisher',
						pattern        : 'build/reports/findbugs/*.xml',
						canRunOnFailed : true
					])
					step([
						$class         : 'PmdPublisher',
						pattern        : 'build/reports/pmd/*.xml',
						canRunOnFailed : true
					])
					step([
						$class: 'CheckStylePublisher', 
						pattern: 'build/reports/checkstyle/*.xml',
						canRunOnFailed : true
					])
				}
			}
        }
		
		stage('Test') {
            steps {
                sh './gradlew --no-daemon check'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Build') {
            steps {
                sh './gradlew --no-daemon build'
            }
        }

        stage('Update Docker UAT image') {
            when { branch "master" }
            steps {
                sh '''
					docker login -u "amritendudockerhub" -p "Passw1rd"
                    docker build --no-cache -t person .
                    docker tag person:latest amritendudockerhub/person:latest
                    docker push amritendudockerhub/person:latest
					docker rmi person:latest
                '''
            }
        }

        stage('Update UAT container') {
            when { branch "master" }
            steps {
                sh '''
					docker login -u "amritendudockerhub" -p "Passw1rd"
                    docker pull amritendudockerhub/person:latest
					docker stop person
					docker rm person
					docker run -p 9090:9090 --name person -t -d amritendudockerhub/person
					docker rmi -f $(docker images -q --filter dangling=true)
                '''
            }
        }

        stage('Release Docker image') {
            when { buildingTag() }
            steps {
                sh '''
					docker login -u "amritendudockerhub" -p "Passw1rd"
                    docker build --no-cache -t person .
                    docker tag person:latest amritendudockerhub/person:${TAG_NAME}
                    docker push amritendudockerhub/person:${TAG_NAME}
					docker rmi $(docker images -f “dangling=true” -q)
               '''
            }
        }
    }
}
