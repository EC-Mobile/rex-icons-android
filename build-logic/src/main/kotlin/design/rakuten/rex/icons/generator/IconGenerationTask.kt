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

package design.rakuten.rex.icons.generator

import androidx.compose.material.icons.generator.Icon
import androidx.compose.material.icons.generator.IconProcessor
import androidx.compose.material.icons.generator.IconWriter
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
internal abstract class IconGenerationTask : DefaultTask() {

    /**
     * Directory containing raw drawables. These icons will be processed to generate programmatic
     * representations.
     */
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputDirectory
    abstract val allIconsDirectory: DirectoryProperty

    /**
     * Checked-in API file for the generator module, where we will track all the generated icons.
     */
    @get:PathSensitive(PathSensitivity.NONE)
    @get:InputFile
    abstract val expectedApiFile: RegularFileProperty

    /**
     * Root build directory for this task, where outputs will be placed into.
     */
    @get:Internal
    abstract val buildDirectory: DirectoryProperty

    /**
     * Generated API file that will be placed in the build directory. This can be copied manually
     * to [expectedApiFile] to confirm that API changes were intended.
     */
    @get:OutputFile
    val generatedApiFile: File
        get() = buildDirectory.file(API_FILE_PATH).get().asFile

    /**
     * @return a list of all processed [Icon]s.
     */
    private fun loadIcons(): List<Icon> {
        return IconProcessor(
            allIconsDirectory.get().asFile.listFiles()!!.filter { it.isDirectory },
            expectedApiFile.get().asFile,
            generatedApiFile,
        ).process()
    }

    @get:OutputDirectory
    val generatedSrcMainDirectory: File
        get() = buildDirectory.dir(GENERATED_SRC_MAIN).get().asFile

    /**
     * The action for this task
     */
    @TaskAction
    fun run() {
        IconWriter(loadIcons()).generateTo(generatedSrcMainDirectory)
    }

    companion object {
        const val TASK_NAME = "generateIcons"
        const val TASK_DESCRIPTION = "Convert icons from xml to Compose ImageVectors."
        const val API_FILE_PATH = "api/icons.txt"
        private const val GENERATED_SRC_MAIN = "src/commonMain/kotlin"
    }
}
