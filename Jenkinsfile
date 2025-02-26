pipeline {
	agent any
	
	environment {
		DEV_DEST_DIR = '/home/oauth2/instance/staging/webapps/oauth2.war'
		WAR_FILE = 'oauth2.war'
		REMOTE_USER = 'hadi'
		REMOTE_HOST = 'localhost'
		CREDENTIALS_ID = 'hadi'
        EXCLUDE_FOLDERS = '.env,.kotlin,gradle'

        JAVA_HOME = '/home/jenkins-agent/.sdkman/candidates/java/current'
        GRAALVM_HOME = '/home/jenkins-agent/.sdkman/candidates/java/current'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
	}

	stages {

        stage('Check Environment') {
            steps {
                script {
                    sh 'pwd'
                    sh 'whoami'
                    sh 'java -version'
                    sh 'echo $SDKMAN_DIR'
                    sh 'echo $NVM_DIR'
                    sh 'npm -v'
                }
            }
        }

        stage('Detect Changes') {
            steps {
                script {
                    // Mendapatkan daftar file yang berubah
                    def changes = sh(script: "git diff --name-only HEAD~1", returnStdout: true).trim().split("\n")

                    // Cek apakah ada perubahan di backend atau frontend
                    env.BUILD_SERVICE1 = changes.any { it.startsWith("backend/api") } ? 'true' : 'true'
                    env.BUILD_SERVICE2 = changes.any { it.startsWith("backend/notification") } ? 'true' : 'true'
                    env.BUILD_WEB1 = changes.any { it.startsWith("frontend/todo") } ? 'true' : 'true'
                }
            }
        }

        stage('Build Backend Service 1') {
            when {
                expression { env.BUILD_SERVICE1 == 'true' }
            }
            steps {
                dir('backend') {
				    sh 'pwd'
				    sh 'java -version'
                    sh './gradlew :api:nativeCompile'
                }
            }
        }

        stage('Build Backend Service 2') {
            when {
                expression { env.BUILD_SERVICE2 == 'true' }
            }
            steps {
                dir('backend') {
                    sh './gradlew :notification:build'
                }
            }
        }

        stage('Build Frontend Web1') {
            when {
                expression { env.BUILD_WEB1 == 'true' }
            }
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
				sh 'ls -al target/'
				sh 'mvn install'
			}
		}
		stage('Deploy') {
			steps {
				sshagent(credentials: [CREDENTIALS_ID]) {
					sh """
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