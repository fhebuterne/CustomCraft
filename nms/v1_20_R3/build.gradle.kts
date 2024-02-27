plugins {
    id("java")
}

dependencies {
    implementation(project(":nms:interfaces"))
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly(files("../../tmp/1.20.4/spigot-1.20.4-R0.1-SNAPSHOT.jar"))
    compileOnly(files("../../tmp/1.20.4/datafixerupper-6.0.8.jar"))
}