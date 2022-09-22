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

package testutils

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

const val TEST_RESOURCE_PATH = "src/test/resources/"
const val DIST_INCLUDE_PATH = "src/dist/"
const val DIST_LANGDEMO_PATH = "${DIST_INCLUDE_PATH}examples/langdemo/template"

fun getLangDemoStream(file: String): FileInputStream =
    FileInputStream(File(File(DIST_LANGDEMO_PATH), file))


fun getResourceStream(file: String): FileInputStream =
    FileInputStream(File(File(TEST_RESOURCE_PATH), file))

fun getStringStream(input: String): InputStream =
    ByteArrayInputStream(input.toByteArray())

fun readResource(file: String): String {
    FileInputStream(File(File(TEST_RESOURCE_PATH), file)).use {
        return String(it.readAllBytes())
    }
}


