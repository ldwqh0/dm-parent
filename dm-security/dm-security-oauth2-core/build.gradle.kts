plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.springframework.security:spring-security-oauth2-core")
    implementation(project(":collections"))
    implementation(project(":dm-security-core"))

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    compileOnly("org.springframework:spring-webflux")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
}
