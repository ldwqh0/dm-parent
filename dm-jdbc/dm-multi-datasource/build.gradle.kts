plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    // TODO 要想办法拿掉这个依赖
    implementation("org.springframework:spring-core")
    implementation("com.zaxxer:HikariCP")
    implementation("org.springframework.data:spring-data-jdbc")
    implementation("org.hibernate:hibernate-core")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation(project(":collections"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
