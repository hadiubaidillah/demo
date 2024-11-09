pipeline {
	agent any
	
	environment {
		DEV_DEST_DIR = '/home/oauth2/instance/staging/webapps/oauth2.war'
		WAR_FILE = 'oauth2.war'
		REMOTE_USER = 'hadi'
		REMOTE_HOST = 'localhost'
		CREDENTIALS_ID = 'hadi'
        EXCLUDE_FOLDERS = '.env,.kotlin,gradle'
	}

	stages {
        stage('Detect Changes') {
            steps {
                script {
                    // Mendapatkan daftar file yang berubah
                    def changes = sh(script: "git diff --name-only HEAD~1", returnStdout: true).trim().split("\n")

                    // Cek apakah ada perubahan di backend atau frontend
                    if (changes.any { it.startsWith("backend/api") }) {
                        env.BUILD_SERVICE1 = 'true'
                    }
                    if (changes.any { it.startsWith("backend/notification") }) {
                        env.BUILD_SERVICE2 = 'true'
                    }
                    if (changes.any { it.startsWith("frontend/todo") }) {
                        env.BUILD_WEB1 = 'true'
                    }
                }
            }
        }
        stage('Build Backend Services') {
            when { environment name: 'BUILD_SERVICE1', value: 'true' }
            steps {
                dir('backend/api') {
                    sh './gradlew build'
                }
            }
            when { environment name: 'BUILD_SERVICE2', value: 'true' }
            steps {
                dir('backend/notification') {
                    sh './gradlew build'
                }
            }
        }
        stage('Build Frontend Applications') {
            when { environment name: 'BUILD_WEB1', value: 'true' }
            steps {
                dir('frontend/todo') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }
		/*stage("Prepare") {
			steps {
				echo("Start Job : ${env.JOB_NAME}")
				echo("Start Build : ${env.BUILD_NUMBER}")
				echo("Branch Name : ${env.BRANCH_NAME}")
				sh('cp -f src/main/resources/application.properties.development src/main/resources/application.properties')
			}
		}
		stage('Build') {
			steps {
				sh 'pwd'
				sh 'ls -al target/'
				sh 'mvn install'
			}
		}
		stage('Deploy') {
			steps {
				sshagent(credentials: [CREDENTIALS_ID]) {
					sh """
					ssh ${REMOTE_USER}@${REMOTE_HOST} 'whoami'
					scp -o StrictHostKeyChecking=no target/${WAR_FILE} ${REMOTE_USER}@${REMOTE_HOST}:${DEV_DEST_DIR}
					ssh -o StrictHostKeyChecking=no -t ${REMOTE_USER}@${REMOTE_HOST} "nohup ~/jetty_restart.sh > /dev/null 2>&1 & disown"
					"""
				}
			}
		}*/
	}

	post {
		always {
			echo "I will always say Hello again!"
		}
		success {
			echo "Yay, success"
		}
		failure {
			echo "Oh no, failure"
		}
		cleanup {
			echo "Don't care success or error"
		}
	}
}