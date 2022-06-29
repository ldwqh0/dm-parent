plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation(project(":dm-common"))
    implementation(project(":dm-data"))
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework:spring-web")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("com.querydsl:querydsl-jpa")
    implementation(project(":collections"))
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("jakarta.validation:jakarta.validation-api")
    annotationProcessor("com.querydsl:querydsl-apt:${Configuration.Versions.queryDsl}:jpa")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    compileOnly("org.springframework.security:spring-security-core")
    compileOnly(project(":dm-security-core"))
}
tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "dm.uap.common")
    }
}
