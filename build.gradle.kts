plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.freefair.lombok") version "6.5.0.3"
    base
    java
}

allprojects {
    group = "fr.fabienhebuterne.customcraft"
    version = "1.4.0"

    apply(plugin = "base")
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
        compileOnly("com.google.code.gson:gson:2.8.9")
        testImplementation("org.mockito:mockito-core:4.7.0")
        testImplementation("org.mockito:mockito-junit-jupiter:4.7.0")
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}