plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.logging.log4j:log4j-core:2.14.0")

    // For JSON Config POC
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    // For Rolling Pocs
    testImplementation("org.mockito:mockito-core:5.11.0")

}

tasks.test {
    useJUnitPlatform()
}