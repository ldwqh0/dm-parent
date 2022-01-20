plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework.security:spring-security-oauth2-client")

    implementation(project(":collections"))
    implementation(project(":dm-security-core"))
    implementation(project(":dm-security-oauth2-core"))
    implementation(project(":dm-security-web"))

    compileOnly("org.springframework:spring-webflux")
    compileOnly("io.projectreactor:reactor-core")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}
