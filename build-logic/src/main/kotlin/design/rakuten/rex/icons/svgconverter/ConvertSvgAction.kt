/*
 * Copyright 2024 Rakuten Group, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package design.rakuten.rex.icons.svgconverter

import com.android.ide.common.vectordrawable.Svg2Vector
import org.gradle.workers.WorkAction
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import kotlin.io.path.name

internal abstract class ConvertSvgAction : WorkAction<ConvertSvgParameters> {

    companion object {
        private const val REX_PREFIX = "rexicon_"
        private const val SVG_EXT = ".svg"
        private const val XML_EXT = ".xml"

        private val SIZE_ATTRS = setOf("android:width", "android:height")
        private const val ORIGINAL_SIZE = "\"32dp\""
        private const val TARGET_SIZE = "\"24dp\""
    }

    override fun execute() {
        runCatching {
            val srcPath = parameters.srcFile.get().asFile.toPath()
            val name = srcPath.name
                .removePrefix(REX_PREFIX)
                .removeSuffix(SVG_EXT)
                .replace('-', '_')
                .plus(XML_EXT)

            var xmlContent = ByteArrayOutputStream().apply {
                use {
                    Svg2Vector.parseSvgToXml(srcPath, it)
                }
            }.toString(Charsets.UTF_8)
            for (attr in SIZE_ATTRS) {
                xmlContent = xmlContent.replace(
                    "$attr=$ORIGINAL_SIZE",
                    "$attr=$TARGET_SIZE"
                )
            }

            val dst = parameters.dstDir.file(name).get().asFile.toPath()
            Files.newOutputStream(dst).writer().use {
                it.write(xmlContent)
            }
        }.onFailure {
            throw RuntimeException(it)
        }
    }
}
