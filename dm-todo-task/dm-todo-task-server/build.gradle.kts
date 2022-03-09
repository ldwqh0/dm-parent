plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation(project(":dm-common"))
    implementation(project(":dm-data"))
    implementation(project(":dm-todo-task-api"))
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework:spring-web")
    implementation("org.springframework.security:spring-security-core")
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("com.querydsl:querydsl-jpa")
    implementation(project(":collections"))

    implementation("jakarta.validation:jakarta.validation-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    annotationProcessor("com.querydsl:querydsl-apt:${Configuration.Versions.queryDsl}:jpa")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
}
