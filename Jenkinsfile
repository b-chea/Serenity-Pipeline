pipeline {
	agent any
	environment {
		JIRA_SITE = 'https://bethsaidach-1738694022756.atlassian.net'
		JIRA_ISSUE_KEY = 'PLPROJECT1'
		JIRA_URL = "${JIRA_SITE}/rest/api/3/issue"
		XRAY_URL = "${JIRA_SITE}/rest/raven/2.0/api"
	}

	tools {
		maven 'MAVEN_HOME'
		jdk 'JAVA_HOME'
	}

	stages {
		stage('Build') {
			steps {
				echo "Building..."
			}
		}

		stage('Compilar proyecto') {
			steps {
				bat 'mvn compile'
			}
		}

		stage('Create Test Execution in Jira') {
			steps {
				script {
					withCredentials([usernamePassword(credentialsId: 'jenkins-credentials-local', usernameVariable: 'JIRA_USER', passwordVariable: 'JIRA_AUTH_PSW')]) {
						def authHeader = "Basic " + "${JIRA_USER}:${JIRA_AUTH_PSW}".bytes.encodeBase64().toString()

						bat """
                            curl -X POST ^
                            -H "Authorization: ${authHeader}" ^
                            -H "Content-Type: application/json" ^
                            -H "Accept: application/json" ^
                            --data "{ \\"fields\\": { \\"project\\": { \\"key\\": \\"PLPROJECT1\\" }, \\"summary\\": \\"Ejecución de pruebas automatizadas\\", \\"description\\": { \\"type\\": \\"doc\\", \\"version\\": 1, \\"content\\": [{\\"type\\": \\"paragraph\\", \\"content\\": [{\\"type\\": \\"text\\", \\"text\\": \\"Ejecución de test automatizados desde Jenkins\\"}]}] }, \\"issuetype\\": { \\"name\\": \\"Test Execution\\" } } }" ^
                            "${JIRA_URL}" > response.json
                        """

						def key = bat(script: 'type response.json | findstr /C:"key"', returnStdout: true).trim()
						key = key.replaceAll('.*"key":"([^"]+)".*', '$1')
						env.TEST_EXECUTION_KEY = key

						echo "Test Execution Key: ${env.TEST_EXECUTION_KEY}"
					}
				}
			}
		}

		stage('Preparar Tests') {
			steps {
				script {
					// Crear directorios si no existen
					bat """
                        if not exist "C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\SerenityPipeline\\src\\test\\java\\runner" mkdir "C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\SerenityPipeline\\src\\test\\java\\runner"
                    """

					// Copiar los tests
					bat """
                        xcopy "C:\\Users\\user\\IdeaProjects\\Expo\\src\\test\\java\\runner\\*.*" "C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\SerenityPipeline\\src\\test\\java\\runner\\" /S /E /I /Y
                    """
				}
			}
		}

		stage('Ejecutar Pruebas') {
			steps {
				script {
					try {
						bat 'mvn test'
						env.TEST_STATUS = 'PASSED'
					} catch (Exception e) {
						env.TEST_STATUS = 'FAILED'
						env.ERROR_MESSAGE = e.getMessage()
						error("Test execution failed: ${e.getMessage()}")
					}
				}
			}
		}

		stage('Update Test Results in Xray') {
			steps {
				script {
					withCredentials([usernamePassword(credentialsId: 'jenkins-credentials-local', usernameVariable: 'JIRA_USER', passwordVariable: 'JIRA_AUTH_PSW')]) {
						def authHeader = "Basic " + "${JIRA_USER}:${JIRA_AUTH_PSW}".bytes.encodeBase64().toString()

						echo "Updating test status for key: ${env.TEST_EXECUTION_KEY}"

						bat """
                            curl -X PUT ^
                            -H "Authorization: ${authHeader}" ^
                            -H "Content-Type: application/json" ^
                            -H "Accept: application/json" ^
                            --data "{ \\"status\\": \\"${env.TEST_STATUS}\\", \\"comment\\": \\"Test ejecutado desde Jenkins\\" }" ^
                            "${XRAY_URL}/test/${env.TEST_EXECUTION_KEY}/status"
                        """

						if (env.TEST_STATUS == 'FAILED') {
							bat """
                                curl -X POST ^
                                -H "Authorization: ${authHeader}" ^
                                -H "Content-Type: application/json" ^
                                -H "Accept: application/json" ^
                                --data "{ \\"fields\\": { \\"project\\": { \\"key\\": \\"PLPROJECT1\\" }, \\"summary\\": \\"Fallo en pruebas automatizadas\\", \\"description\\": { \\"type\\": \\"doc\\", \\"version\\": 1, \\"content\\": [{\\"type\\": \\"paragraph\\", \\"content\\": [{\\"type\\": \\"text\\", \\"text\\": \\"Error en la ejecución: ${env.ERROR_MESSAGE}\\"}]}] }, \\"issuetype\\": { \\"name\\": \\"Bug\\" }, \\"customfield_10016\\": [\\"${env.TEST_EXECUTION_KEY}\\"] } }" ^
                                "${JIRA_URL}"
                            """
						}
					}
				}
			}
		}
	}

	post {
		success {
			echo 'Test execution completed successfully and results updated in Jira!'
		}
		failure {
			echo 'Test execution failed. Bug created in Jira with details.'
		}
	}
}