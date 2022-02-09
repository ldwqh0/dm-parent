plugins {
    id("com.dm.java-conventions")
}
dependencies {
    api(project(":dm-notification-api"))

    implementation(project(":collections"))

    implementation("jakarta.validation:jakarta.validation-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
