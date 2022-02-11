group = "com.dm"
version = Configuration.Versions.project

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    mavenCentral()
}

plugins {
    `java-library`
    `maven-publish`
    `eclipse`
    id("io.spring.dependency-management")
}

eclipse {
    sourceSets {
        main {
            java {
                srcDir("build/generated/sources/annotationProcessor/java/main")
            }
        }
    }

    classpath {
        plusConfigurations.plus(configurations.compileOnly)
    }
}


dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${Configuration.Versions.springBoot}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Configuration.Versions.springCloud}")
    }
    dependencies {
        dependencies {
            dependency("commons-io:commons-io:${Configuration.Versions.commons_io}")
            dependency("com.querydsl:querydsl-jpa:${Configuration.Versions.queryDsl}")
            dependency("org.jsoup:jsoup:${Configuration.Versions.jsoup}")
            dependency("com.github.binarywang:weixin-java-mp:${Configuration.Versions.weixin_java_mp}")
            dependency("com.nimbusds:oauth2-oidc-sdk:${Configuration.Versions.nimbusds}")
        }
    }
}



java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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
            val releasesRepoUrl = Configuration.Publish.releasesRepoUrl
            val snapshotsRepoUrl = Configuration.Publish.snapshotsRepoUrl
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            isAllowInsecureProtocol = true
            credentials {
                username = Configuration.Publish.username
                password = Configuration.Publish.password
            }
        }
        mavenLocal()
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.withType<Test> {
    useJUnitPlatform()
}
