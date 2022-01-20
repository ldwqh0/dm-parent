plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.springframework.security:spring-security-core")
    implementation("org.springframework.security:spring-security-web")
    implementation(project(":collections"))

    api("com.github.binarywang:weixin-java-mp")

    implementation("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("jakarta.servlet:jakarta.servlet-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}
