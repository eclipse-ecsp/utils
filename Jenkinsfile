def RELEASE_NAME = "ignite-utils"
def COMPONENT_NAME = "ignite-utils"
def CHARTMUSEUM
def CHARTMUSEUM_SNAPSHOT = "chartmuseum-snapshot"
def CHARTMUSEUM_RELEASE = "chartmuseum-release"
def CHART_VERSION
def NAMESPACE = "default"
def COMP_VERSION

pipeline {

	options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    agent {
      kubernetes {
        cloud 'k8s-devops'
        defaultContainer 'build-agent'
        inheritFrom 'jnlp-agent-jdk-17-ignite'
      }
    }

	parameters {
              string (name: 'EMAIL_RECIPIENTS', defaultValue: 'shalaka.deshmukh@harman.com', trim: true )
            }

	environment {
      	BRANCH = " "
		SKIP_TESTS = "false"		//false - to run with tests , true - to skip tests
		SONAR_COMMAND = "sonar:sonar -Dsonar.login=ignite_jenkins -Dsonar.password=Abcd1234 -Dsonar.host.url=https://sonarqube-staging.nh.ad.harman.com/"
		MVN_EXTRA_COMMAND = "-Dmaven.test.skip=false"
	}

	stages {
      
      stage('Prepare Maven Settings') {
			steps {
				script {		
					withAWS(credentials:'nh_jenkins_ignite_aws',region:'us-east-1') {
						s3Download(file:"mvn_settings.xml", bucket:"jenkins-ignite", path:"maven/mvn_settings_global.xml", force:true)
					}
                }
            }
        }
      
		stage('Prepare Environment') {
			steps {
				script {
					// move all job parameters to environment variables
					params.each { k, v -> env[k] = v }
					BRANCH = "${env.BRANCH_NAME}"
					withEnv(["BRANCHNAME=" + BRANCH ]) {
					    sshagent(['Bitbucket']) {
                            sh "git branch -r"
                            sh "git checkout ${BRANCHNAME}"
                        }
					}
					//update mvn command
					if (SKIP_TESTS == "false") {
					    MVN_EXTRA_COMMAND = SONAR_COMMAND
                    }
             	}
          	}
      	}

      stage('Building') {
			steps {
              	script {
                  dir ("${env.WORKSPACE}") {
                    BRANCH = "${env.BRANCH_NAME}"
                    sh "ls"
					POM_VERSION = readMavenPom().getVersion()
					COMMIT = sh (script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    //if (BRANCH == 'master') {
						COMP_VERSION = "${POM_VERSION}"
                    //} else {
                      //  COMP_VERSION = "${POM_VERSION}-${COMMIT}-SNAPSHOT"
                    //}
                  }
				}
				echo "COMP_VERSION = ${COMP_VERSION}"
                sh "mvn versions:set -DnewVersion=${COMP_VERSION} -s mvn_settings.xml "
              	sh "mvn clean install -Dmaven.test.failure.ignore=true sonar:sonar -Dsonar.host.url=https://sonarqube-staging.nh.ad.harman.com/ -Dsonar.login=ignite_jenkins -Dsonar.password=Abcd1234 -U -s mvn_settings.xml"

			}
		}

        stage('Deploy') {
        steps {
         sh "mvn clean deploy " + MVN_EXTRA_COMMAND + " -U -s mvn_settings.xml "
         }
        }

      stage('Blackduck') {
            steps {
				script {
					dir("${env.WORKSPACE}") {
					sh "wget https://detect.synopsys.com/detect7.sh"
                    sh "chmod 777 detect7.sh"
                    sh "env"
					BLACKDUCK_PROJECT_NAME = "${readMavenPom().getArtifactId()}-${POM_VERSION}"

                    sh "./detect7.sh --blackduck.url='https://blackduckhub.harman.com' --blackduck.api.token='YTdmNDg1NDItNzA5Mi00Y2Q0LTk1M2ItYmZlYmZmZTY5NzE4OjIxNTJkOGM3LTM5ZmItNDI2Ni05OTNkLTkxMzFhYjI0MDkxYg==' --detect.source.path=${env.WORKSPACE} --detect.project.name=Ignite-core-db-2.42.0 --detect.project.version.phase='RELEASED' --detect.project.version.name=${BLACKDUCK_PROJECT_NAME} --detect.code.location.name=${BLACKDUCK_PROJECT_NAME} --detect.cleanup=true --blackduck.offline.mode=false --blackduck.trust.cert=true --blackduck.timeout=1200 --detect.maven.path='/usr/bin/mvn' --detect.project.codelocation.unmap=true"
                   }
				}
            }
        }
  	}
  	 post {
                 failure {
                     mail bcc: '', body: "Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL to build: ${env.BUILD_URL}", \
                     cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "Build ${currentBuild.result} -> ${env.BUILD_NUMBER} - ${env.JOB_NAME}", to: env.EMAIL_RECIPIENTS;
                 }
                 success {
                      mail bcc: '', body: "Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL to build: ${env.BUILD_URL}", \
                      cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "Build ${currentBuild.result} -> ${env.BUILD_NUMBER} - ${env.JOB_NAME}", to: env.EMAIL_RECIPIENTS;
                 }
                 }

}
