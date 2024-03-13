/*
 * Copyright 2020 The Android Open Source Project
 * Copyright 2024 Rakuten Group, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.material.icons.generator

import java.io.File

/**
 * Generates programmatic representation of all [icons] using [ImageVectorGenerator].
 *
 * @property icons the list of [Icon]s to generate Kotlin files for
 */
class IconWriter(private val icons: List<Icon>) {
    /**
     * Generates icons and writes them to [outputSrcDirectory].
     *
     * @param outputSrcDirectory the directory to generate source files in
     */
    fun generateTo(
        outputSrcDirectory: File
    ) {
        icons.forEach { icon ->
            val vector = IconParser(icon).parse()

            val fileSpec = ImageVectorGenerator(
                icon.kotlinName,
                icon.theme,
                vector
            ).createFileSpec()

            fileSpec.writeToWithCopyright(outputSrcDirectory)

            // Write additional file specs for auto-mirrored icons. These files will be written into
            // an automirrored package and will hold a similar icons theme structure underneath.
            if (vector.autoMirrored) {
                val autoMirroredFileSpec = ImageVectorGenerator(
                    icon.kotlinName,
                    icon.theme,
                    vector
                ).createAutoMirroredFileSpec()

                autoMirroredFileSpec.writeToWithCopyright(outputSrcDirectory)
            }
        }
    }
}
