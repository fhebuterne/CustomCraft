plugins {
    id("java")
}

dependencies {
    implementation(project(":nms:interfaces"))
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly(files("../../libs/spigot-1.20.1-R0.1-SNAPSHOT.jar"))
}