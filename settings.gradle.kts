rootProject.name = "spring-messenger"

include("backend", "frontend", "bot", "shared")

pluginManagement {
	val springBootVersion = "2.2.0.RELEASE"
	repositories {
		maven { url = uri("https://repo.spring.io/milestone") }
		maven { url = uri("https://repo.spring.io/snapshot") }
		gradlePluginPortal()
	}
	resolutionStrategy {
		eachPlugin {
			if (requested.id.id == "org.springframework.boot") {
				useModule("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
			}
		}
	}
	plugins {
		val kotlinVersion = "1.3.50"
		id("org.jetbrains.kotlin.js") version kotlinVersion apply false
		id("org.jetbrains.kotlin.multiplatform") version kotlinVersion  apply false
		id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
		id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion apply false

		id("org.springframework.boot") version springBootVersion apply false
		id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
	}
}