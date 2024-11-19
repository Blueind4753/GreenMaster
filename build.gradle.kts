plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.gradleup.shadow") version "8.3.5"
}

group = "me.blueind"
version = "1.0"

repositories {
    mavenCentral()
}

javafx {
    version = "21.0.5"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.slf4j:slf4j-simple:2.0.16")

    // pi4j
    implementation("com.pi4j:pi4j-core:2.7.0")
    implementation("com.pi4j:pi4j-plugin-raspberrypi:2.7.0")

    // OpenCV
    implementation("org.bytedeco:opencv-platform:4.6.0-1.5.8")

    // TensorFlow
    implementation("org.bytedeco:tensorflow-platform:1.15.5-1.5.8")
}

application {
    mainClass.set("me.blueind.Main") // 애플리케이션 메인 클래스 설정
}

tasks.shadowJar {
    mergeServiceFiles()  // 서비스 파일 병합
    manifest {
        attributes(
            "Main-Class" to "me.blueind.Main"  // 실행 시 메인 클래스를 지정
        )
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)  // 빌드 시 ShadowJar 생성 작업을 실행
}

tasks.test {
    useJUnitPlatform()
}