plugins {
    java
    jacoco
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.suel"
version = "1.0.0-SNAPSHOT"
description = "checkpoint-java-nv1-alura"

tasks.jar {
    archiveBaseName.set("app")
    archiveVersion.set("")
}

tasks.bootJar {
    archiveBaseName.set("app")
    archiveVersion.set("")
}

jacoco {
    toolVersion = "0.8.14"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.jacocoTestReport {
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.5".toBigDecimal()
            }
        }

        rule {
            isEnabled = false
            element = "CLASS"
            includes = listOf("org.gradle.*")

            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "0.3".toBigDecimal()
            }
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    runtimeOnly("org.springframework.boot:spring-boot-docker-compose")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("net.datafaker:datafaker:2.5.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
