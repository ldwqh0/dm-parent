plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("com.nimbusds:oauth2-oidc-sdk")
    implementation(project(":dm-common"))
    implementation(project(":dm-autoconfigure"))
    implementation(project(":dm-security-core"))
    implementation(project(":dm-security-web"))
    implementation(project(":dm-security-oauth2-resource"))
}
