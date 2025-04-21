plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.compose") version "1.5.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
    application
}

group = "com.sudoku"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
}

application {
    mainClass.set("com.sudoku.MainKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.named("run").configure {
    enabled = false
}

tasks.register<Jar>("fatJar") {
    archiveBaseName.set("${project.name}-all")
    manifest {
        attributes["Main-Class"] = "com.sudoku.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

tasks.register("createMSI") {
    dependsOn("fatJar")
    doLast {
        val outputDir = layout.buildDirectory.dir("msi")
        outputDir.get().asFile.mkdirs()

        exec {
            commandLine(
                "jpackage",
                "--input", "build/libs",
                "--name", project.name,
                "--main-jar", "${project.name}-${project.version}-all.jar",
                "--main-class", "com.sudoku.MainKt",
                "--type", "msi",
                "--destination", outputDir,
                "--jvm-options", "--add-modules", "java.desktop"
            )
        }
    }
}
