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

package testdiagram

import com.cjsoftware.antlr4docgen.parser.DefaultStreamProvider
import com.cjsoftware.antlr4docgen.parser.GrammarModelBuilder
import com.cjsoftware.antlr4docgen.railroad.Diagram
import com.cjsoftware.antlr4docgen.railroad.DiagramSettings
import com.cjsoftware.antlr4docgen.railroad.buildDiagram
import org.junit.jupiter.api.Test
import testutils.TEST_RESOURCE_PATH
import java.io.File

class TestDiagram {

    @Test
    fun testKotlinDiagram() {
        val grammarPath = File("$TEST_RESOURCE_PATH/antlr_kotlin/KotlinParser.g4")
        val parser = GrammarModelBuilder(DefaultStreamProvider(grammarPath.parentFile))
        val diagram = Diagram(
            "Kotlin syntax diagram generated from grammar at https://github.com/antlr/grammars-v4/tree/master/kotlin/kotlin",
            DiagramSettings.processStyleFile(requireNotNull(this::class.java.getResourceAsStream("default_styles.txt"))),
            buildDiagram(parser.processGrammar(grammarPath))
        )

        File("./docs/kotlin.html").writeText(diagram.render())
    }

    @Test
    fun testSwiftDiagram() {
        val grammarPath = File("$TEST_RESOURCE_PATH/antlr_swift5/Swift5Parser.g4")
        val parser = GrammarModelBuilder(DefaultStreamProvider(grammarPath.parentFile))
        val diagram = Diagram(
            "Swift 5 syntax diagram generated from grammar at https://github.com/antlr/grammars-v4/tree/master/swift/swift5",
            DiagramSettings.processStyleFile(requireNotNull(this::class.java.getResourceAsStream("default_styles.txt"))),
            buildDiagram(parser.processGrammar(grammarPath))
        )

        File("./docs/swift5.html").writeText(diagram.render())
    }

    @Test
    fun testTypescriptDiagram() {
        val grammarPath = File("$TEST_RESOURCE_PATH/antlr_typescript/TypeScriptParser.g4")
        val parser = GrammarModelBuilder(DefaultStreamProvider(grammarPath.parentFile))
        val diagram = Diagram(
            "Typescript syntax diagram generated from grammar at https://github.com/antlr/grammars-v4/tree/master/javascript/typescript",
            DiagramSettings.processStyleFile(requireNotNull(this::class.java.getResourceAsStream("default_styles.txt"))),
            buildDiagram(parser.processGrammar(grammarPath))
        )

        File("./docs/typescript.html").writeText(diagram.render())
    }

}