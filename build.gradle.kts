plugins {
    id("fabric-loom") version "1.11.8"
    id("maven-publish")
    id("io.github.juuxel.loom-quiltflower") version "1.6.0"
    id("io.github.p03w.machete") version "2.0.0"
}

loom {
    mixin {
        defaultRefmapName = "moborigins.refmap.json"
    }
}

val archivesBaseName: String by project
val modVersion: String by project
val mavenGroup: String by project

group = mavenGroup
version = modVersion
base.archivesName.set(archivesBaseName)

tasks {
    processResources {
        inputs.property("version", version)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to version))
        }

        val environment = System.getenv("ENVIRONMENT") ?: "production"
        println("Environment: $environment")

        if (environment != "production") {
            from(rootProject.rootDir.absolutePath + "/testdata") { into("data") }
        }
    }

    jar {
        from("LICENSE") {
            rename { "LICENSE_${archivesBaseName}" }
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}

repositories {
    mavenCentral()
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    maven {
        name = "Quilt"
        url = uri("https://maven.quiltmc.org/repository/release")
    }
    maven {
        name = "Quilt Snapshots"
        url = uri("https://maven.quiltmc.org/repository/snapshot")
    }
    maven {
        name = "Minecraft"
        url = uri("https://libraries.minecraft.net/")
    }
    maven(url = "https://api.modrinth.com/maven")
    maven(url = "https://maven.terraformersmc.com/")
    maven(url = "https://jitpack.io")
    maven(url = "https://maven.jamieswhiteshirt.com/libs-release/")
    maven(url = "https://maven.shedaniel.me/")
    maven(url = "https://maven.ladysnake.org/releases")
    mavenLocal()
}


java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.1")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc.fabric-api:fabric-api:0.116.7+1.21.1")
    modImplementation("net.fabricmc:fabric-loader:0.16.9")

    modImplementation(files("libs/Apoli-2.12.0-pre.1+mc.1.21.1.jar"))
    modImplementation(files("libs/origins-1.13.0-pre.1+mc.1.21.1.jar"))
    modImplementation(files("libs/Calio-1.14.0-alpha.9+mc.1.21.x.jar"))

    modImplementation("org.quiltmc.parsers:json:0.2.1")
    modImplementation("org.quiltmc.parsers:gson:0.2.1")

    modImplementation(libs.modmenu)

    modImplementation ("dev.onyxstudios.cardinal-components-api:cardinal-components-base:6.1.0")
    modImplementation ("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:6.1.0")
    modCompileOnly ("com.google.code.findbugs:jsr305:3.0.2")
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:11.0.99")
    modImplementation(files("libs/pal-1.10.0.jar"))
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}