plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation("org.springframework.security:spring-security-core")
    implementation("org.springframework.security:spring-security-web")
    implementation(project(":collections"))
    implementation(project(":dm-security-core"))
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    compileOnly("io.projectreactor:reactor-core")
    compileOnly("jakarta.servlet:jakarta.servlet-api")

}
tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "dm.security.web")
    }
}
