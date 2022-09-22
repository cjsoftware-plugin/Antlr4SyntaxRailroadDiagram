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

package testvisitors

import com.cjsoftware.antlr4docgen.parser.DefaultStreamProvider
import com.cjsoftware.antlr4docgen.parser.GrammarModelBuilder
import org.junit.jupiter.api.Test
import testutils.TEST_RESOURCE_PATH
import java.io.File

class TestAntlr4DocGen {
    @Test
    fun testGrammarVisitor() {
        val parser = GrammarModelBuilder(DefaultStreamProvider(File("$TEST_RESOURCE_PATH/antlr")))
        parser.processGrammar(File("GFTRecipeParser.g4"))
    }
}