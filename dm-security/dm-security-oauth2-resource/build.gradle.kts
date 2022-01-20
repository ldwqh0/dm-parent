plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation(project(":dm-security-core"))
    implementation(project(":dm-security-oauth2-core"))
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("org.slf4j:slf4j-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    compileOnly("org.springframework:spring-webflux")

}
