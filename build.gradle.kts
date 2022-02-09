group = "com.dm"
version = V.V.project
plugins {
    `java-platform`
    `maven-publish`
}
javaPlatform {
    allowDependencies()
}
dependencies {
    constraints {
        runtime("jakarta.activation:jakarta.activation-api:1.2.2")
    }
}
publishing {
    publications.create<MavenPublication>("maven") {
        from(components["javaPlatform"])
    }
    repositories {
        mavenLocal()
    }
}
