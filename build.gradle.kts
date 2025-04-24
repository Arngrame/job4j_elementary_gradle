plugins {
    application
    checkstyle
    java
    jacoco
    id("com.github.spotbugs") version "6.0.26"
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

application {
    mainClass.set("ru/job4j/Main.java")
}

tasks.register<Zip>("zipJavaDoc") {
    group = "documentation" // Группа, в которой будет отображаться задача
    description = "Packs the generated Javadoc into a zip archive"

    dependsOn("javadoc") // Указываем, что задача зависит от выполнения javadoc

    from("build/docs/javadoc") // Исходная папка для упаковки
    archiveFileName.set("javadoc.zip") // Имя создаваемого архива
    destinationDirectory.set(layout.buildDirectory.dir("archives")) // Директория, куда будет сохранен архив
}

tasks.spotbugsMain {
    reports.create("html") {
        required = true
        outputLocation.set(layout.buildDirectory.file("reports/spotbugs/spotbugs.html"))
    }
}

tasks.test {
    finalizedBy(tasks.spotbugsMain)
}

tasks.register("checkJarSize") {
    group = "verification"
    description = "Checks the size of the generated JAR file."

    dependsOn("jar") // Задача зависит от сборки JAR

    val pathToJar = "build/libs/${project.name}-${project.version}.jar"
    val jarFile = file(pathToJar) // Путь к JAR-файлу

    doLast {
        if (jarFile.exists()) {
            val sizeInMB = jarFile.length() / (1024 * 1024) // Размер в мегабайтах
            if (sizeInMB > 5) {
                println("WARNING: JAR file exceeds the size limit of 5 MB. Current size: ${sizeInMB} MB")
            } else {
                println("JAR file is within the acceptable size limit. Current size: ${sizeInMB} MB")
            }
        } else {
            println("JAR file not found. Please make sure the build process completed successfully.")
        }
    }
}
