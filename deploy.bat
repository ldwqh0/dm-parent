git.exe push --progress "origin" 0.2.x:0.2.x
git.exe push --progress "aliyun" 0.2.x:develop
git.exe push --progress "aliyun" 0.2.x:master
mvn clean deploy -Dmaven.test.skip=true