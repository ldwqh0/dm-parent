plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    compileOnly("io.projectreactor:reactor-core")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    api("org.springframework.security:spring-security-core")
    api("org.springframework.security:spring-security-web")
    api("org.springframework:spring-web")
    api(project(":dm-security-core"))
    // https://mvnrepository.com/artifact/io.projectreactor/reactor-core

    // https://mvnrepository.com/artifact/org.springframework.security/spring-security-oauth2-resource-server
    // TODO 这里不应该有这
    implementation("org.springframework.security:spring-security-oauth2-resource-server")


    compileOnly("jakarta.servlet:jakarta.servlet-api")
//    implementation("com.jhlabs:filters:2.0.235-1")
//    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation(project(":collections"))
}
