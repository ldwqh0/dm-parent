plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    implementation("commons-io:commons-io")
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("org.hibernate:hibernate-core")
    implementation("commons-codec:commons-codec")
    implementation("com.querydsl:querydsl-jpa")

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("org.slf4j:slf4j-api")
    implementation("com.fasterxml.jackson.core:jackson-annotations")

    implementation(project(":collections"))
    implementation(project(":dm-common"))

    annotationProcessor("com.querydsl:querydsl-apt:${Configuration.Versions.queryDsl}:jpa")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}
