plugins {
    id("com.dm.java-conventions")
}
dependencies {
    api("org.springframework.security:spring-security-core")
    api("org.springframework:spring-web")
    implementation("com.jhlabs:filters:2.0.235-1")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("org.apache.commons:commons-lang3")


    implementation(project(":collections"))
}
