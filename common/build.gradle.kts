plugins {
    id("java")
    id("io.freefair.lombok")
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    implementation(project(":nms:interfaces"))
    implementation(project(":nms:v1_18_R2"))
    implementation(project(":nms:v1_19_R1"))
}