plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation(project(":collections"))
    implementation(project(":dm-common"))
    implementation(project(":dm-data"))
    implementation(project(":dm-notification-api"))
    implementation("org.springframework:spring-web")
    implementation("org.springframework.security:spring-security-core")
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("jakarta.validation:jakarta.validation-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
