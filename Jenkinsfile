def projectFolder = "cm.lao.devops.playground"
def projectName = "devops-playground"
def pipelineUtils
def VERSION
def isMasterBranch
def isDevelopBranch
def gitlabCredentials = "jenkins-ssh-credentials-gitlab"
def isTriggeredManually
def scmVariables
def imageName = ""
def userId
def userGroupId

node {
    properties([
            pipelineTriggers([
                    [
                            $class                       : 'GitLabPushTrigger',
                            triggerOnMergeRequest        : true,
                            triggerOpenMergeRequestOnPush: 'source',
                            branchFilterType             : 'NameBasedFilter',
                            excludeBranchesSpec          : "master",
                    ]
            ]),
            disableConcurrentBuilds()
    ])
    skipStagesAfterUnstable()

    scmVariables = checkout([
            $class           : 'GitSCM',
            branches         : [[name: env.BRANCH_NAME]],
            extensions       : [
                    [$class: 'WipeWorkspace'],
                    [
                            $class             : 'SubmoduleOption',
                            disableSubmodules  : false,
                            parentCredentials  : true,
                            recursiveSubmodules: true,
                            reference          : '',
                            trackingSubmodules : false
                    ]
            ],
            userRemoteConfigs: [[url: 'git@gitlab.lao-sarl.cm:microservice/cm.lao.devops.playground.git']]
    ])

    try {
        updateGitlabCommitStatus name: 'BUILD', state: 'running'
        isMasterBranch = env.BRANCH_NAME == "master";
        isDevelopBranch = env.BRANCH_NAME == "develop";
        stage("SCM-GIT tag and push preparation") {
            if (isDevelopBranch) {
                sshagent (credentials: [gitlabCredentials]) {
                    sh("git checkout master")
                    sh("git merge origin/develop")
                }
            }
        }

        stage("INIT") {
            pipelineUtils = load "submodules/cm.lao.common.build/groovy/PipelineUtils.groovy"
            userId = sh(returnStdout: true, script: "id -u").trim()
            userGroupId = sh(returnStdout: true, script: "id -g")
            if (isMasterBranch) {
                sh "mkdir -p ${WORKSPACE}/jib"
            }
        }

        withEnv([
                "NEXUS_DOCKER_IMAGE_NAME_PREFIX=nexus.lao-sarl.cm",
                "NEXUS_DOCKER_REGISTRY_URL=nexus.lao-sarl.cm",
                "ON_JENKINS=true"
        ]) {
            docker.image("gradle:8.7.0-jdk21-alpine")
                    .inside(
                            '' +
                                    "-u root:$userGroupId " +
                                    '-w /home/gradle ' +
                                    '-v $HOME/.gradle:/home/gradle/.gradle ' +
                                    '-v /var/run/docker.sock:/var/run/docker.sock ' +
                                    '-v /usr/bin/docker:/usr/bin/docker ' +
                                    '--env DOCKER_HOST=tcp://localhost:2375 ' +
                                    '--network="host"'
                    ) {
                        try {
                            stage("BUILD") {
                                isTriggeredManually = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause').size() > 0;
                                dir(projectFolder) {
                                    VERSION = sh(returnStdout: true, script: "gradle properties -q | grep \"version:\" | awk '{print \$2}'")
                                    imageName = "${projectName}:${VERSION}".trim()
                                    try {
                                        sh "gradle clean classes"
                                    } catch (Throwable e) {
                                        currentBuild.result = 'FAILURE'
                                        throw e;
                                    }
                                }
                            }
                            if (!isMasterBranch) {
                                stage("TESTS") {
                                    try {
                                        dir(projectFolder) {
                                            sh "gradle test"
                                            sh "gradle dataTest"
                                            sh "gradle e2eTest"
                                        }
                                    } catch (Throwable e) {
                                        e.printStackTrace()
                                        unstable("TEST FAILED")
                                    } finally {
                                        junit "**/build/test-results/**/TEST*.xml,**/build/cucumber-reports/**/cucumber.xml"
                                        cucumber(
                                                buildStatus: 'UNSTABLE',
                                                failedFeaturesNumber: 1,
                                                failedScenariosNumber: 1,
                                                skippedStepsNumber: 1,
                                                failedStepsNumber: 1,
                                                reportTitle: projectFolder,
                                                fileIncludePattern: '**/cucumber-reports/json/*.json',
                                                sortingMethod: 'ALPHABETICAL',
                                                trendsLimit: 100
                                        )
                                    }
                                }
                            }

                            withCredentials([usernamePassword(
                                    credentialsId: 'jenkins-nexus-credentials',
                                    usernameVariable: 'NEXUS_CREDENTIALS_USR',
                                    passwordVariable: 'NEXUS_CREDENTIALS_PSW'
                            )]) {
                                withEnv([
                                        "DOCKER_REPOSITORY=nexus.lao-sarl.cm",
                                        "NEXUS_CREDENTIALS_USR=${NEXUS_CREDENTIALS_USR}",
                                        "NEXUS_CREDENTIALS_PSW=${NEXUS_CREDENTIALS_PSW}",
                                        "XDG_CONFIG_HOME=${WORKSPACE}/jib"
                                ]) {
                                    if (pipelineUtils.isCurrentBuildSuccessful() && isMasterBranch) {
                                        stage("PUBLISH TO DOCKER") {
                                            catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                                                try {
                                                    dir(projectFolder) {
                                                        sh "gradle jib -Djib.useOnlyProjectCache=true"
                                                    }
                                                } catch (Throwable e) {
                                                    e.printStackTrace()
                                                    throw e
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Throwable throwable) {
                            throwable.printStackTrace()
                        } finally {
                            stage("REPAIR PERMISSION ON GRADLE DIRECTORIES") {
                                sh "chown -R $userId $pwd"
                                sh "chown -R $userId /home/gradle/.gradle"
                            }
                        }
                    }

            if (pipelineUtils.isCurrentBuildSuccessful()) {
                if (isDevelopBranch) {
                    stage("PROMOTE TO RELEASE CANDIDATE") {
                        sshagent(credentials: [gitlabCredentials]) {
                            sh("git push origin master")
                        }
                    }
                }
                if (isMasterBranch) {
                    stage("SEAL RELEASE") {
                        currentBuild.keepLog = true
                        sshagent(credentials: [gitlabCredentials]) {
                            sh("git tag ${VERSION}")
                            sh("git push origin --tags")
                        }
                    }
                }
            }
        }
    } catch(Throwable e) {
        e.printStackTrace()
        throw e
    } finally {
        if (pipelineUtils.isCurrentBuildSuccessful()) {
            updateGitlabCommitStatus name: 'BUILD', state: 'success'
        } else if (currentBuild.result == 'UNSTABLE' || currentBuild.result == 'FAILURE') {
            updateGitlabCommitStatus name: 'BUILD', state: 'failed'
        } else if (currentBuild.result == 'ABORTED') {
            updateGitlabCommitStatus name: 'BUILD', state: 'canceled'
        }
        stage("CLEANUP") {
            pipelineUtils.cleanup()
        }${projectGitUrl}
        stage("NOTIFICATIONS") {
            if (pipelineUtils.isCurrentBuildSuccessful()) {
                if (isMasterBranch) {
                    currentBuild.description = "RELEASE ${VERSION} (${scmVariables.GIT_COMMIT})"
                } else {
                    currentBuild.description = "VERSION: ${VERSION} (${scmVariables.GIT_COMMIT})"
                }
            }
            pipelineUtils.notifyBuild('developers@lao-sarl.cm')
        }
    }
}
