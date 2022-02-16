plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.slf4j:slf4j-api")
    implementation("org.springframework:spring-web")
    implementation("commons-codec:commons-codec")
    implementation(project(":collections"))
}
tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "dm.dingding.api")
    }
}
