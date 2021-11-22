plugins {
    id("com.dm.java-conventions")
}
dependencies {
    api("com.nimbusds:oauth2-oidc-sdk")
    api(project(":dm-common"))
    api(project(":dm-autoconfigure"))
    api(project(":dm-security-core"))
    api(project(":dm-security-web"))
    api(project(":dm-security-oauth2-resource"))
}
