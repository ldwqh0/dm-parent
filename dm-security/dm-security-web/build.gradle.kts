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

    compileOnly("io.projectreactor:reactor-core")
    compileOnly("jakarta.servlet:jakarta.servlet-api")

}
