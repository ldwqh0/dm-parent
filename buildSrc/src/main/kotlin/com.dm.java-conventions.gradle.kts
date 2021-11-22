repositories {
    mavenLocal()
    mavenCentral()
}
group = "com.dm"
version = V.V.project

plugins {
    `java-library`
    `maven-publish`
    id("io.spring.dependency-management")

}


java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${V.V.springBoot}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${V.V.springCloud}")
    }
    dependencies {
        dependencies {
            dependency("commons-io:commons-io:2.11.0")
            dependency("com.querydsl:querydsl-jpa:${V.V.queryDsl}")
            dependency("org.jsoup:jsoup:1.14.3")
            dependency("com.github.binarywang:weixin-java-mp:4.1.0")
        }
    }
}

java {
    withSourcesJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        versionMapping {
            usage("java-api") {
                fromResolutionOf("runtimeClasspath")
            }
            usage("java-runtime") {
                fromResolutionResult()
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = "http://demo.yzhxh.com:8081/nexus/repository/maven-releases/"
            val snapshotsRepoUrl = "http://demo.yzhxh.com:8081/nexus/repository/maven-snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            isAllowInsecureProtocol = true
            credentials {
                username = "lidong"
                password = "lidong"
            }
        }
        mavenLocal()
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
