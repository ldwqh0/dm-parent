plugins {
    id("com.dm.java-conventions")
}
dependencies {
    api("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    api("com.nimbusds:oauth2-oidc-sdk")
    api(project(":dm-common"))
    api(project(":dm-autoconfigure"))
    api(project(":dm-security-core"))
    api(project(":dm-security-oauth2-core"))
    api(project(":dm-security-web"))
    api(project(":dm-security-oauth2-resource"))
}
