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
import java.nio.file.Files
import kotlin.io.path.name

internal abstract class ConvertSvgAction : WorkAction<ConvertSvgParameters> {

    companion object {
        private const val REX_PREFIX = "rexicon_"
        private const val SVG_EXT = ".svg"
        private const val XML_EXT = ".xml"
    }

    override fun execute() {
        runCatching {
            val srcPath = parameters.srcFile.get().asFile.toPath()
            val name = srcPath.name
                .removePrefix(REX_PREFIX)
                .removeSuffix(SVG_EXT)
                .replace('-', '_')
                .plus(XML_EXT)
            val dst = parameters.dstDir.file(name).get().asFile.toPath()
            Files.newOutputStream(dst).use {
                Svg2Vector.parseSvgToXml(srcPath, it)
            }
        }.onFailure {
            throw RuntimeException(it)
        }
    }
}
