/*
 * Copyright (c) 2022 Chris James.
 *    This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

plugins {
    kotlin("jvm") version "1.7.10"
    `java-gradle-plugin`
    antlr
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.0.0"
}

group = "io.github.cjsoftware-plugin"
version = getBuildVersion()

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:4.11.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.compileKotlin {
    dependsOn(tasks["generateGrammarSource"])
}

tasks.compileTestKotlin {
    dependsOn(tasks["generateTestGrammarSource"])

}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments.addAll(
        arrayOf(
            "-visitor",
            "-no-listener",
            "-package", "com.cjsoftware.antlr4docgen.parser",
            "-lib", "$projectDir/antlrshared"
        )
    )
}

tasks.compileKotlin {
    dependsOn(tasks["generateGrammarSource"])
}

fun getBuildVersion(): String =
    Properties().apply {
        FileInputStream(file("$rootDir/version.properties")).use {
            load(it)
        }
    }.let {
        val buildNo = it["VERSION_BUILD"].let {
            if (it != null) {
                if (it is String) it.toInt() + 1 else 0
            } else {
                throw GradleException("Unable to read VERSION_BUILD")
            }
        }
        val major = it["VERSION_MAJOR"].let {
            it?.toString() ?: throw GradleException("Unable to read VERSION_MAJOR")
        }
        val minor = it["VERSION_MINOR"].let {
            it?.toString() ?: throw GradleException("Unable to read VERSION_MINOR")
        }
        FileOutputStream(file("version.properties")).use { fso ->
            it.store(fso, "")
        }
        "$major.$minor.${buildNo.toString().padStart(4, '0')}"
    }

gradlePlugin {
    plugins {
        create("Antlr4SyntaxRailroadDiagramPlugin") {
            id = "$group.${project.name}"
            implementationClass = "com.cjsoftware.antlr4docgen.Antlr4SyntaxRailroadDiagramPlugin"
            displayName = "Antlr4 Syntax Railroad Diagram"
            description = "Generate Syntax Railroad Diagram from Antlr4 Grammar"
        }
    }
}

pluginBundle {
    website = "https://cjsoftware-plugin.github.io/Antlr4SyntaxRailroadDiagram"
    vcsUrl = "https://github.com/cjsoftware-plugin/Antlr4SyntaxRailroadDiagram"
    tags = listOf("antlr", "docs", "documentation")
}