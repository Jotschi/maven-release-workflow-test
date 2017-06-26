
properties([
        parameters([
                booleanParam(name: 'skipTests',            defaultValue: false,  description: "Whether to skip the unit tests"),
                booleanParam(name: 'skipDocker', defaultValue: false, description: "Whether to skip the docker build.")
        ])
])


stash includes: '**', name: 'project'

node('jenkins-slave') {

    stage("Checkout") {
      checkout scm
      stash includes: '**', name: 'project'
    }

    stage('Set Version') {
      def originalV = version();
      def major = originalV[1];
      def minor = originalV[2];
      def patch  = Integer.parseInt(originalV[3]) + 1;
      def v = "${major}.${minor}.${patch}"
      if (v) {
        echo "Building version ${v}"
      }
      sh "mvn -B versions:set -DgenerateBackupPoms=false -DnewVersion=${v}"
      sh 'git add .'
      sh "git commit -m 'Raise version'"
      sh "git tag v${v}"
    }

    stage("Test") {
      if (Boolean.valueOf(params.skipTests)) {
        echo "Skipped"
      } else {
        def splits = splitTests parallelism: [$class: 'CountDrivenParallelism', size: 10], generateInclusions: true
        def branches = [:]
        for (int i = 0; i < splits.size(); i++) {
          def current = i
          def split = splits[i]
          branches["split${i}"] = {
            node('jenkins-slave') {
              echo "Preparing slave environment for chunk ${current}"
              unstash 'project'
              writeFile file: (split.includes ? 'inclusions.txt' : 'exclusions.txt'), text: split.list.join("\n")
              writeFile file: (split.includes ? 'exclusions.txt' : 'inclusions.txt'), text: ''
              try {
                sh "mvn -B clean test -Dmaven.test.failure.ignore"
              } finally {
                step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/*.xml'])
              }
            }
          }
        }
        parallel branches
      }
    }

    stage('Release Build') {
      sshagent(['git']) {
        sh "mvn -B -DskipTests clean deploy"
        sh "git push origin " + env.BRANCH_NAME
        sh "git push origin v${v}"
      }
    }

    stage('Docker Build') {
      if (Boolean.valueOf(params.skipDocker)) {
        echo "Skipped"
      } else {
        sh "captain build"
        sh "captain push"
      }
    }
}

def version() {
    def matcher = readFile('pom.xml') =~ '<version>(\\d*)\\.(\\d*)\\.(\\d*)(-SNAPSHOT)*</version>'
    matcher ? matcher[0] : null
}
