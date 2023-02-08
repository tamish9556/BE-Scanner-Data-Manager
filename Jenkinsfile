node("wrk-1") {
    def scmInfo
    def imageTag = "public.ecr.aws/g2t6q4f7/backend:latest"
    stage('Prepare') {
        withCredentials([usernamePassword(credentialsId: 'arti', passwordVariable: 'pass_var', usernameVariable: 'user_var')]) {
           sh "aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws"
        }
    }
    stage('Checkout') {
        scmInfo = checkout scm
        scmInfo['build_url'] = BUILD_URL
    }
    stage('Build') {
        def labelString = scmInfo.collect { k, v -> " --label '$k=$v'" }.join(' ')

        def runMap = [failFast: false]
//        runMap["Tests"] = { sh "mvn test" }
        runMap["Docker"] = { sh "docker build $labelString -t $imageTag ." }
        parallel runMap
    }
    stage('Deploy') {
        if(scmInfo['GIT_BRANCH'] == 'main'){
            sh "docker push $imageTag"
        }
        
        sh "sudo docker system prune -f"
    }

}
