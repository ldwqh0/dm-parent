plugins {
    id("com.dm.java-conventions")
}
dependencies {
    api("org.springframework.security:spring-security-config")
    api("org.springframework.security:spring-security-web")
    api(project(":dm-security-core"))
    api(project(":dm-security-web"))
    api(project(":dm-autoconfigure"))
}
