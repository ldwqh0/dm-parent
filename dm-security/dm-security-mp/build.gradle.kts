plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.springframework.security:spring-security-core")
    implementation("org.springframework.security:spring-security-web")
    implementation(project(":collections"))
//    implementation("com.dm:collections")
    implementation("org.slf4j:slf4j-api")
    implementation("org.apache.commons:commons-lang3")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.core:jackson-databind")
//    api("com.github.binarywang:weixin-java-mp")

    compileOnly("jakarta.servlet:jakarta.servlet-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}
tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "dm.security.mp")
    }
}
