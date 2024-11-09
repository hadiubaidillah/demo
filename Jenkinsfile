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
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Scan Changes') {
            steps {
                script {
                    // Dapatkan daftar file yang berubah sejak commit terakhir
                    def changedFiles = sh(script: "git diff --name-only HEAD HEAD~1", returnStdout: true).trim().split('\n')

                    // Filter file yang berubah untuk folder backend dan frontend
                    def backendChanges = []
                    def frontendChanges = []

                    for (file in changedFiles) {
                        // Periksa apakah file berada di folder backend dan tidak termasuk dalam folder yang dikecualikan
                        if (file.startsWith('backend/') && !EXCLUDE_FOLDERS.tokenize(',').any { file.contains(it) }) {
                            backendChanges.add(file)
                        }

                        // Periksa apakah file berada di folder frontend/todo
                        if (file.startsWith('frontend/todo/')) {
                            frontendChanges.add(file)
                        }
                    }

                    // Set environment variable untuk menentukan apakah ada perubahan
                    env.CHANGED_BACKEND = backendChanges.size() > 0
                    env.CHANGED_FRONTEND = frontendChanges.size() > 0
                }
            }
        }
        stage('Build Backend') {
            when {
                expression { return env.CHANGED_BACKEND == 'true' }
            }
            steps {
                echo 'Building changed backend services...'
                script {
                    sh './gradlew build'
                }
            }
        }
        stage('Build Frontend') {
            when {
                expression { return env.CHANGED_FRONTEND == 'true' }
            }
            steps {
                echo 'Building changed frontend apps...'
                script {
                    dir('frontend/todo') {
                        sh 'npm install && npm run build'
                    }
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