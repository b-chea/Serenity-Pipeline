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

		stage('Crear Incidencia Tipo Test') {
			steps {
				script {
					withCredentials([usernamePassword(credentialsId: 'jenkins-credentials-local', usernameVariable: 'JIRA_USER', passwordVariable: 'JIRA_AUTH_PSW')]) {
						def authHeader = "Basic " + "${JIRA_USER}:${JIRA_AUTH_PSW}".bytes.encodeBase64().toString()

						bat """
                        curl -X POST ^
                        -H "Authorization: ${authHeader}" ^
                        -H "Content-Type: application/json" ^
                        -H "Accept: application/json" ^
                        --data "{ \\"fields\\": { \\"project\\": { \\"key\\": \\"PLPROJECT1\\" }, \\"summary\\": \\"Prueba desde Jenkins\\", \\"description\\": { \\"type\\": \\"doc\\", \\"version\\": 1, \\"content\\": [{\\"type\\": \\"paragraph\\", \\"content\\": [{\\"type\\": \\"text\\", \\"text\\": \\"Creando un issue test desde Jenkins\\"}]}] }, \\"issuetype\\": { \\"name\\": \\"Test\\" } } }" ^
                        "${JIRA_URL}"
                        """
					}
				}
			}
		}

		stage('Leer Excel y Crear Acciones de Prueba') {
			steps {
				script {
					// Aquí usas un script Groovy para leer el Excel y generar las acciones de prueba
					// Esto podría implicar leer cada fila del Excel y crear pruebas basadas en esos datos
					def excelData = readExcel(file: 'src/main/resources/test-data/data.xlsx') // Si usas el plugin Apache POI
					excelData.each { row ->
						// Procesar cada fila, por ejemplo, creando una acción de prueba
						echo "Creando acción de prueba para: ${row['Action']}"
						// Aquí deberías generar las pruebas en función de los datos del Excel
					}
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

		stage('Actualizar Resultado en Xray') {
			steps {
				script {
					withCredentials([usernamePassword(credentialsId: 'jenkins-credentials-local', usernameVariable: 'JIRA_USER', passwordVariable: 'JIRA_AUTH_PSW')]) {
						def authHeader = "Basic " + "${JIRA_USER}:${JIRA_AUTH_PSW}".bytes.encodeBase64().toString()

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
                                --data "{ \\"fields\\": { \\"project\\": { \\"key\\": \\"${JIRA_ISSUE_KEY}\\" }, \\"summary\\": \\"Fallo en pruebas automatizadas\\", \\"description\\": { \\"type\\": \\"doc\\", \\"version\\": 1, \\"content\\": [{\\"type\\": \\"paragraph\\", \\"content\\": [{\\"type\\": \\"text\\", \\"text\\": \\"Error en la ejecución: ${env.ERROR_MESSAGE}\\"}]}] }, \\"issuetype\\": { \\"name\\": \\"Bug\\" }, \\"customfield_10016\\": [\\"${env.TEST_EXECUTION_KEY}\\"] } }" ^
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
