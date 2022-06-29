plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework:spring-web")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("com.querydsl:querydsl-jpa")
    implementation("org.jsoup:jsoup")

    implementation(project(":collections"))
    implementation(project(":dm-common"))

    annotationProcessor("com.querydsl:querydsl-apt:${Configuration.Versions.queryDsl}:jpa")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("jakarta.validation:jakarta.validation-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}
tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "dm.region")
    }
}
