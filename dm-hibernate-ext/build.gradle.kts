plugins {
    id("com.dm.java-conventions")
}

dependencies {
    implementation("org.hibernate:hibernate-core")
    implementation("org.apache.commons:commons-lang3")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "dm.hibernate")
    }
}
