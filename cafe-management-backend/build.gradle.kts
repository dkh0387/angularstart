import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
}

group = "de.dkh"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    //implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.7")
    /*Security stuff: JSON web token, Soring security */
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("org.springframework.boot:spring-boot-starter-security:3.1.2")
    testImplementation("org.springframework.security:spring-security-test:6.1.2")
    /* Sending mails over the network */
    implementation("org.springframework.boot:spring-boot-starter-mail:3.1.2")
    /* Generating/downloading PDFs */
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("org.apache.pdfbox:pdfbox:3.0.0")
    /* String validation etc. */
    implementation("com.google.guava:guava:32.1.2-jre")
    /* Converting JSON-POJO */
    //implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.vaadin.external.google:android-json:0.0.20131108.vaadin1")
    // Needed for XML Serialization of JSON web tokens from credentials
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.sun.xml.bind:jaxb-core:4.0.3")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.3")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    implementation("com.sun.xml.bind:jaxb-impl:4.0.3")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
