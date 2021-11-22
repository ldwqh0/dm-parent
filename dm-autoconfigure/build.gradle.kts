plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.springframework:spring-beans")
    implementation("org.apache.commons:commons-lang3")
    implementation(project(":collections"))
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.data:spring-data-jpa")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework:spring-webflux")
    compileOnly("jakarta.persistence:jakarta.persistence-api")
    compileOnly("javax.cache:cache-api")
    compileOnly("com.querydsl:querydsl-jpa")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")

    compileOnly("org.springframework.security:spring-security-config")
    // https://mvnrepository.com/artifact/org.springframework.security/spring-security-oauth2-client
    // https://mvnrepository.com/artifact/org.springframework.security/spring-security-oauth2-resource-server
    compileOnly("org.springframework.security:spring-security-oauth2-resource-server")

    compileOnly("org.springframework.security:spring-security-oauth2-client")
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    compileOnly("com.github.binarywang:weixin-java-mp")

    compileOnly(project(":dm-file-core"))
    compileOnly(project(":dm-common"))
    compileOnly(project(":dm-security-core"))
    compileOnly(project(":dm-authority-common"))
    compileOnly(project(":dm-multi-datasource"))
    compileOnly(project(":dm-region"))
    compileOnly(project(":dm-multi-datasource-jpa-support"))
    compileOnly(project(":dm-security-web"))
    compileOnly(project(":dm-security-mp"))
    compileOnly(project(":dm-uap-common"))
    compileOnly(project(":dm-security-oauth2-resource"))
    compileOnly(project(":dm-security-oauth2-client"))

}
