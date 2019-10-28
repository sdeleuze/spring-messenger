import org.jetbrains.kotlin.gradle.tasks.KotlinJsDce

plugins {
	id("org.jetbrains.kotlin.js")
}

apply(plugin = "kotlin-dce-js")

repositories {
	mavenCentral()
}

kotlin {
	target {
		compilations.all {
			kotlinOptions {
				moduleKind = "umd"
			}
		}
		browser {
			webpackTask {
				val runDceKotlin by tasks.getting(KotlinJsDce::class)
				dependsOn(runDceKotlin)
			}
		}
	}
}

dependencies {
	implementation(kotlin("stdlib-js"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.2")
	implementation(project(":shared"))
}
