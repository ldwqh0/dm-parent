plugins {
    id("com.dm.java-conventions")
}
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "dm.collections")
    }
}
