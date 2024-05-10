import groovy.text.StreamingTemplateEngine

def updateBuildsKeepState() {
    if (currentBuild.resultIsWorseOrEqualTo("UNSTABLE") || env.BRANCH_NAME == "master") {
        currentBuild.setKeepLog(true)
    }

    previousBuild = currentBuild.previousBuild
    while (previousBuild != null) {
        if (previousBuild.currentResult == null) {
            continue
        }
        if (!previousBuild.resultIsWorseOrEqualTo(currentBuild.currentResult)) {
            break
        }

        previousBuild.setKeepLog(false)
        previousBuild = previousBuild.previousBuild
    }
}

def emailTemplate(params) {

    def fileContents = """
Basic text:
<td>
  \${jenkinsText}
</td>

Link:
<a href="\${jenkinsUrl}"></a>

Conditional:
<td>
  \${statusSuccess ? 'The job is done' : "The job couldn't be done"}
</td>
"""
    def engine = new StreamingTemplateEngine()
    return engine.createTemplate(fileContents).make(params).toString()

}

def notifyBuild(to) {
    try {
        def icon = "✅"
        def statusSuccess = true

        def buildStatus = currentBuild.currentResult ?: 'SUCCESS'

        if (buildStatus != 'SUCCESS') {
            icon = "❌"
            statusSuccess = false
        }
        def body = emailTemplate([
                "jenkinsText"  : env.JOB_NAME,
                "jenkinsUrl"   : env.BUILD_URL,
                "statusSuccess": statusSuccess,
                "hasArtifacts" : false,
                "downloadUrl"  : "www.downloadurl.com"
        ])

        mail(
                to: to,
                subject: "${icon} [ ${env.JOB_NAME} ] [${env.BUILD_NUMBER}] - ${buildStatus} ",
                body: body,
                mimeType: 'text/html'
        )
    } catch (e) {
        println "ERROR SENDING EMAIL ${e}"
    }
}

def cleanup() {
    this.cleanupDockerData()
}

def cleanupDockerData() {
    try {
        sh "docker system prune --force"
    } catch (Throwable ex) {
        echo("exception thrown: ${ex.message}")
    }
}

def findFreePort() {
    ServerSocket serverSocket
    try {
        serverSocket = new ServerSocket(0)
        int localServerPort = serverSocket.getLocalPort()
        echo("using port: " + localServerPort)
        serverSocket.close()
        return localServerPort
    } catch (IOException ex) {
        echo("could not find a free port ${ex.message}")
        throw ex
    }
    finally {
        if (serverSocket != null) {
            try {
                serverSocket.close()
            } catch (IOException ignore) {
            }
        }
    }
}

def getCurrentIp() {
    return InetAddress.localHost.hostAddress
}

def replaceAll(file, text, replacement) {
    writeFile file: file, text: readFile(file).replaceAll(text, replacement)
}

def isCurrentBuildSuccessful() {
    return currentBuild.result == null || currentBuild.result == 'SUCCESS'
}

def ensureAppIsReady(url, maxRetries) {
    def count = 0;
    def isReady = false
    waitUntil(initialRecurrencePeriod: 10000) {
        try {
            sh "curl ${url} | grep UP"
            isReady = true;
            return true
        } catch (Exception e) {
            if (count > maxRetries) {
                return true;
            }
            count++
            return false
        }
    }
    return isReady
}

def readPropertyFromFile(file, key) {
    def props = readProperties file: file
    return props[key]
}

return this
