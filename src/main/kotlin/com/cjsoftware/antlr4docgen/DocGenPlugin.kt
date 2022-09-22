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

package com.cjsoftware.antlr4docgen

import com.cjsoftware.antlr4docgen.parser.DefaultStreamProvider
import com.cjsoftware.antlr4docgen.parser.GrammarModelBuilder
import com.cjsoftware.antlr4docgen.railroad.Diagram
import com.cjsoftware.antlr4docgen.railroad.DiagramSettings
import com.cjsoftware.antlr4docgen.railroad.buildDiagram
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

private const val BASE_NAME = "antlr4SyntaxRailroadDiagram"


open class DocGenPluginExtenstion {
    var inputGrammarName = "parsergrammar"
    var inputGrammarDirectory: File? = null
    var sourceSetName = "main"
    var documentTitle = "Railroad Syntax Diagram"
    var documentName = "railroadsyntaxdiagram.html"
    var curveRadius = 10f
    var innerPadding = 10f
    var pathWidth = 2f
    var ruleStartCharSize = 14f
    var ruleCharSize = 12f
    var literalCharSize = 12f
    var embedStyleFile: File? = null
    var outputDirectory = ""
}

private const val ANTLR4_FILE_EXTENSION = ".g4"

open class Antlr4SyntaxRailroadDiagramPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension: DocGenPluginExtenstion = target.extensions.create(BASE_NAME, DocGenPluginExtenstion::class.java)

        target.tasks.register(BASE_NAME, Antlr4SyntaxRailroadDiagramTask::class.java) {
            // Configure task from extension
            it.documentTitle.set(extension.documentTitle)
            it.documentName.set(extension.documentName)
            it.outputDirectory.set(File(extension.outputDirectory))
            it.inputGrammarName.set(extension.inputGrammarName)
            it.inputGrammarDirectory.set(extension.inputGrammarDirectory)
            it.curveRadius.set(extension.curveRadius)
            it.innerPadding.set(extension.innerPadding)
            it.pathWidth.set(extension.pathWidth)
            it.ruleStartCharSize.set(extension.ruleStartCharSize)
            it.ruleCharSize.set(extension.ruleCharSize)
            it.literalCharSize.set(extension.literalCharSize)
            it.embedStyleFile.set(extension.embedStyleFile)
            it.sourceSetName.set(extension.sourceSetName)
        }
    }

}

abstract class Antlr4SyntaxRailroadDiagramTask : DefaultTask() {

    @get:Input
    abstract val inputGrammarName: Property<String>

    @get:Optional
    @get:Input
    abstract val inputGrammarDirectory: Property<File>

    @get:Input
    abstract val documentTitle: Property<String>

    @get:Input
    abstract val documentName: Property<String>

    @get:Input
    abstract val sourceSetName: Property<String>

    @get:Input
    abstract val curveRadius: Property<Float>

    @get:Input
    abstract val innerPadding: Property<Float>

    @get:Input
    abstract val pathWidth: Property<Float>

    @get:Input
    abstract val ruleStartCharSize: Property<Float>

    @get:Input
    abstract val ruleCharSize: Property<Float>

    @get:Input
    abstract val literalCharSize: Property<Float>

    @get:Optional
    @get:Input
    abstract val embedStyleFile: Property<File>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generateDiagram() {
        logger.info("    Antlr4SyntaxRailroadDiagram  Copyright (C) 2022  Chris James\n    This program comes with ABSOLUTELY NO WARRANTY; for details see the license file on the developer's github repository.")
        val grammarName = if (inputGrammarName.get().endsWith(ANTLR4_FILE_EXTENSION)) {
            inputGrammarName.get()
        } else {
            inputGrammarName.get() + ANTLR4_FILE_EXTENSION
        }

        val grammarPath = File(resolveGrammarDirectory(grammarName), grammarName)
        if (!grammarPath.exists()) throw GradleException("Input grammar must exist (${grammarPath.name})")

        logger.info("Using grammar input file : ${grammarPath.path}")


        val styleDef = if (embedStyleFile.isPresent) {
            DiagramSettings.processStyleFile(embedStyleFile.get().inputStream())
        } else {
            DiagramSettings.processStyleFile(requireNotNull(this::class.java.getResourceAsStream("default_styles.txt")))
        }

        // Apply settings from task parameters
        DiagramSettings.curveRadius = curveRadius.get()
        DiagramSettings.innerPadding = innerPadding.get()
        DiagramSettings.pathWidth = pathWidth.get()
        DiagramSettings.ruleStartCharSize = ruleStartCharSize.get()
        DiagramSettings.ruleCharSize = ruleCharSize.get()
        DiagramSettings.literalCharSize = literalCharSize.get()


        val outputFile = File(outputDirectory.get().asFile, documentName.get())
        outputFile.canonicalFile.parentFile.apply {
            if (!exists() && !mkdirs()) throw GradleException("Cannot create output path (${outputFile.name})")
        }

        logger.info("Writing output to file : ${outputFile.path}")

        val modelBuilder = GrammarModelBuilder(DefaultStreamProvider(grammarPath.parentFile))
        val diagram = Diagram(
            documentTitle.get(),
            styleDef,
            buildDiagram(modelBuilder.processGrammar(grammarPath))
        )

        outputFile.writeText(diagram.render())

    }

    private fun resolveGrammarDirectory(grammarName: String): File {
        if (inputGrammarDirectory.isPresent) {
            return inputGrammarDirectory.get().let {
                if (it.exists()) it
                else throw GradleException("Input grammar directory must exist: ${it.name}")
            }
        } else {
            val grammarDirectory = findInputDirectory(sourceSetName.get(), grammarName)
            if (grammarDirectory == null) throw GradleException("Unable to locate directory containing ${inputGrammarName.get()} in ${sourceSetName.get()} sourceset")
            else return grammarDirectory
        }
    }

    private fun findInputDirectory(sourceSetName: String, fileName: String): File? {

        project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.getByName(sourceSetName).allSource.forEach { sd ->
            if (sd.name == fileName) return sd.parentFile
        }

        return null
    }
}


