plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("jakarta.validation:jakarta.validation-api")

    compileOnly("com.fasterxml.jackson.core:jackson-annotations")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
