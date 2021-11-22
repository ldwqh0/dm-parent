plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework.security:spring-security-core")
    implementation("jakarta.validation:jakarta.validation-api")
//    implementation("commons-io:commons-io")
    implementation("org.springframework:spring-web")
//    implementation("org.springframework:spring-webmvc")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("jakarta.persistence:jakarta.persistence-api")
//    implementation("org.hibernate:hibernate-core")
//    implementation("commons-codec:commons-codec")
    implementation("com.querydsl:querydsl-jpa")
//    compileOnly("jakarta.annotation:jakarta.annotation-api")
//    compileOnly("org.slf4j:slf4j-api")
//    compileOnly("jakarta.validation:jakarta.validation-api")
//
    implementation(project(":collections"))
    implementation(project(":dm-common"))
    compileOnly(project(":dm-security-core"))

    annotationProcessor("com.querydsl:querydsl-apt:${V.V.queryDsl}:jpa")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api:1.3.5")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api:2.2.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}
