plugins {
    id("io.freefair.lombok") version "6.5.0.3"
    base
    java
}

allprojects {
    group = "fr.fabienhebuterne.customcraft"
    version = "1.2.0"

    apply(plugin = "base")
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
        implementation("com.google.code.gson:gson:2.8.6")
        testImplementation("org.mockito:mockito-core:4.7.0")
        testImplementation("org.mockito:mockito-junit-jupiter:4.7.0")
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    tasks.withType<Jar> {
        destinationDirectory.set(file(System.getProperty("outputDir") ?: "$rootDir/build/"))
    }

    tasks.withType<ProcessResources> {
        filesMatching("**/**.yml") {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            expand(project.properties)
        }
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}