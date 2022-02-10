group = "com.dm"
version = V.V.project

repositories {
    maven {
        url = uri("https://maven.aliyun.com/repository/public/")
    }
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
        mavenBom("org.springframework.boot:spring-boot-dependencies:${V.V.springBoot}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${V.V.springCloud}")
    }
    dependencies {
        dependencies {
            dependency("commons-io:commons-io:${V.V.commons_io}")
            dependency("com.querydsl:querydsl-jpa:${V.V.queryDsl}")
            dependency("org.jsoup:jsoup:${V.V.jsoup}")
            dependency("com.github.binarywang:weixin-java-mp:${V.V.weixin_java_mp}")
            dependency("com.nimbusds:oauth2-oidc-sdk:${V.V.nimbusds}")
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
tasks.withType<Test> {
    useJUnitPlatform()
}
