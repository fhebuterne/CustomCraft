plugins {
    id("java")
    id("io.freefair.lombok")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    implementation(project(":nms:interfaces"))
    implementation(project(":nms:v1_18_R2"))
    implementation(project(":nms:v1_19_R1"))
    implementation(project(":nms:v1_19_R2"))
    implementation(project(":nms:v1_19_R3"))
    implementation(project(":nms:v1_20_R1"))
    implementation(project(":nms:v1_20_R2"))
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

tasks.shadowJar {
    archiveFileName.set("CustomCraft-${archiveVersion.getOrElse("unknown")}.jar")
}

tasks.build {
    dependsOn("shadowJar")
}