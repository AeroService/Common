pipeline {
    agent any

    stages {
        stage("Clean") {
            steps {
                sh "chmod +x ./gradlew";
                sh "./gradlew clean";
            }
        }
        stage("Build") {
            steps {
                sh "./gradlew shadowJar";
            }
        }
        stage("Publish") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  sh "./gradlew publish -DpublishPassword=$PASSWORD -DpublishName=$USERNAME"
                }
                discordSend description: '', footer: '', image: '', link: '', result: '', scmWebUrl: '', thumbnail: '', title: '', webhookURL: 'https://discord.com/api/webhooks/997287075755859978/2dbdXwh6XDakfRHb35dPeQVQSUF1OqJviJ-FHq_lB8FwffoUL31lF0g07jLYcH_h9RZH'
            }
        }
    }
}
