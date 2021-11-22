plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.security:spring-security-core")
    implementation("org.springframework.data:spring-data-commons")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("com.querydsl:querydsl-jpa")

    annotationProcessor("com.querydsl:querydsl-apt:${V.V.queryDsl}:jpa")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api:1.3.5")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api:2.2.3")
    implementation(project(":collections"))
    implementation(project(":dm-common"))
    implementation(project(":dm-security-core"))
}
