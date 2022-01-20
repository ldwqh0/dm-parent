plugins {
    `kotlin-dsl`
}
repositories {
    maven {
        url = uri("https://maven.aliyun.com/repository/gradle-plugin")
    }
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
    mavenCentral()
}

dependencies {
    implementation(kotlin("script-runtime"))
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
}
