plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
    // TODO 要想办法拿掉这个依赖
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-context")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("com.querydsl:querydsl-jpa")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation(project(":dm-common"))
    implementation(project(":collections"))
    api(project(":dm-multi-datasource"))

    annotationProcessor("com.querydsl:querydsl-apt:${V.V.queryDsl}:jpa")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api:1.3.5")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api:2.2.3")

    compileOnly("com.fasterxml.jackson.core:jackson-annotations")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}
