plugins {
    id("com.dm.java-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-lang3")
    implementation(project(":collections"))

    compileOnly("org.springframework:spring-web")
    compileOnly("jakarta.validation:jakarta.validation-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "dm.common")
    }
}
