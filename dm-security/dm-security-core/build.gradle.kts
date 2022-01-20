plugins {
    id("com.dm.java-conventions")
}
dependencies {

    implementation("org.springframework.security:spring-security-core")
    implementation("org.springframework.security:spring-security-web")
    // 这个依赖是给验证码用的
    implementation("com.jhlabs:filters:2.0.235-1")
    implementation("org.apache.commons:commons-lang3")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation(project(":collections"))

}
