plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    implementation(project(":collections"))

    compileOnly("org.springframework.data:spring-data-commons")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("jakarta.persistence:jakarta.persistence-api")
    compileOnly("org.springframework.data:spring-data-jpa")
    compileOnly("org.springframework:spring-web")
    compileOnly("jakarta.validation:jakarta.validation-api")
    compileOnly("com.querydsl:querydsl-jpa")
    compileOnly("org.hibernate:hibernate-core")

    annotationProcessor("com.querydsl:querydsl-apt:${V.V.queryDsl}:jpa")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
