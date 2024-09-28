plugins {
    id("java")
}

group = "ru.unlegit"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("com.github.pengrad:java-telegram-bot-api:7.9.1")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}