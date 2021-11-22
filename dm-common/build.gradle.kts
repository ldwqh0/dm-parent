plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.springframework.data:spring-data-commons")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("org.springframework:spring-web")
    implementation("org.apache.commons:commons-lang3")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation(project(":collections"))

    compileOnly("com.querydsl:querydsl-jpa")
    compileOnly("org.hibernate:hibernate-core")

    annotationProcessor("com.querydsl:querydsl-apt:${V.V.queryDsl}:jpa")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api:1.3.5")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api:2.2.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
