plugins {
    `kotlin-dsl`
}
repositories {
    mavenLocal()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
    mavenCentral()
}

dependencies {
    implementation(kotlin("script-runtime"))
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
}
