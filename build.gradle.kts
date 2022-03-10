group = "com.dm"
version = Configuration.Versions.project
plugins {
    `java-platform`
    `maven-publish`
}
javaPlatform {
    allowDependencies()
}
dependencies {
    api(platform("org.springframework.cloud:spring-cloud-dependencies:${Configuration.Versions.springCloud}"))
    api(platform("org.springframework.boot:spring-boot-dependencies:${Configuration.Versions.springBoot}"))
    constraints {
        api(project(":collections"))
        api(project(":dm-common"))
        api(project(":dm-data"))
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

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["javaPlatform"])
    }
    repositories {
        maven {
            val releasesRepoUrl = "https://packages.aliyun.com/maven/repository/2124183-release-zS40pt"
            val snapshotsRepoUrl = "https://packages.aliyun.com/maven/repository/2124183-snapshot-jg10er"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            isAllowInsecureProtocol = true
            credentials {
                username = "5f213c551d62073ceeb332b2"
                password = "NC=d3W)28)]W"
            }
        }
    }
}
