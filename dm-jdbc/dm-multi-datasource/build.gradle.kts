plugins {
    id("com.dm.java-conventions")
}
dependencies {
    // TODO 要想办法拿掉这个依赖
//    implementation("org.springframework:spring-core")
    implementation("org.apache.commons:commons-lang3")
    implementation("com.zaxxer:HikariCP")
    implementation("org.springframework.data:spring-data-jdbc")
    implementation("org.hibernate:hibernate-core")

    implementation(project(":collections"))

    implementation("jakarta.validation:jakarta.validation-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
