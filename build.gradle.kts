plugins {
    application
    checkstyle
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(libs.org.junit.jupiter.junit.jupiter.engine)
    testImplementation(libs.org.junit.vintage.junit.vintage.engine)
    testImplementation(libs.org.assertj.assertj.core)
}

group = "ru.job4j"
version = "1.0"
description = "elementary"
java.sourceCompatibility = JavaVersion.VERSION_21

tasks.test {
    useJUnitPlatform()
}

checkstyle {
    toolVersion = "10.3.3"
    configFile = file("checkstyle.xml")
    isIgnoreFailures = false
}
