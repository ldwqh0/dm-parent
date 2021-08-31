git.exe push --progress "origin" 2.2.x:2.2.x
git.exe push --progress "origin" master:master
git.exe push --progress "aliyun" 2.2.x:develop
git.exe push --progress "aliyun" master:master
mvn clean deploy -Dmaven.test.skip=true
