git.exe push --progress "origin" 0.2.1-SNAPSHOT:0.2.1-SNAPSHOT
git.exe push --progress "aliyun" 0.2.1-SNAPSHOT:develop
git.exe push --progress "aliyun" 0.2.1-SNAPSHOT:master
mvn clean deploy -Dmaven.test.skip=true