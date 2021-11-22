plugins {
    id("com.dm.java-conventions")
}
dependencies {
    implementation("org.apache.commons:commons-lang3")
//    implementation("org.springframework.security:spring-security-core")
//    implementation("org.springframework.security:spring-security-web")
//    implementation("jakarta.validation:jakarta.validation-api")
////    implementation("commons-io:commons-io")
//    implementation("org.springframework:spring-web")
    compileOnly("org.springframework:spring-webflux")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations")
//    implementation("org.springframework.data:spring-data-jpa")
//    implementation("jakarta.persistence:jakarta.persistence-api")
////    implementation("org.hibernate:hibernate-core")
////    implementation("commons-codec:commons-codec")
//    implementation("com.querydsl:querydsl-jpa")
////    compileOnly("jakarta.annotation:jakarta.annotation-api")
////    compileOnly("org.slf4j:slf4j-api")
////    compileOnly("jakarta.validation:jakarta.validation-api")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
////
//    implementation(project(":collections"))
//    implementation(project(":dm-common"))
    compileOnly("org.slf4j:slf4j-api")
    implementation(project(":dm-security-core"))
    implementation(project(":dm-security-oauth2-core"))
//
//    annotationProcessor("com.querydsl:querydsl-apt:${V.V.queryDsl}:jpa")
//    annotationProcessor("jakarta.annotation:jakarta.annotation-api:1.3.5")
//    annotationProcessor("jakarta.persistence:jakarta.persistence-api:2.2.3")
//

//    api("com.github.binarywang:weixin-java-mp")

//    compileOnly("jakarta.servlet:jakarta.servlet-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}
