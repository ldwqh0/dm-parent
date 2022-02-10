group = "com.dm"
version = V.V.project
plugins {
    `java-platform`
    `maven-publish`
}
javaPlatform {
    allowDependencies()
}
dependencies {
    api(platform("org.springframework.cloud:spring-cloud-dependencies:${V.V.springCloud}"))
    api(platform("org.springframework.boot:spring-boot-dependencies:${V.V.springBoot}"))
    constraints {
        api(project(":dm-common"))
        api(project(":collections"))
        api(project(":dm-file-core"))
        api(project(":dm-file-stream"))
        api(project(":dm-file-spring-boot-starter"))
        api(project(":dm-autoconfigure"))
        api(project(":dm-authority-common"))
        api(project(":dm-security-core"))
        api(project(":dm-security-web"))
        api(project(":dm-multi-datasource"))
        api(project(":dm-security-mp"))
        api(project(":dm-security-oauth2-core"))
        api(project(":dm-security-oauth2-resource"))
        api(project(":dm-security-oauth2-client"))
        api(project(":dm-security-spring-boot-starter"))
        api(project(":dm-oauth2-resource-server-spring-boot-starter"))
        api(project(":dm-oauth2-client-spring-boot-starter"))
        api(project(":dm-multi-datasource-jpa-support"))
        api(project(":dm-region"))
        api(project(":dm-uap-common"))
        api(project(":dm-notification-api"))
        api(project(":dm-notification-client"))
        api(project(":dm-notification-server"))
        api(project(":dm-todo-task-api"))
        api(project(":dm-todo-task-client"))
        api(project(":dm-todo-task-server"))
    }
}

configurations.all {
    exclude("org.springframework.boot", "spring-boot-starter-tomcat")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["javaPlatform"])
    }
    repositories {
        maven {
            val releasesRepoUrl = "http://demo.yzhxh.com:8081/nexus/repository/maven-releases/"
            val snapshotsRepoUrl = "http://demo.yzhxh.com:8081/nexus/repository/maven-snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            isAllowInsecureProtocol = true
            credentials {
                username = "lidong"
                password = "lidong"
            }
        }
        mavenLocal()
    }
}
