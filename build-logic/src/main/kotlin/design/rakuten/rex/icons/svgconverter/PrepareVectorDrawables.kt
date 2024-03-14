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

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.submit
import org.gradle.work.DisableCachingByDefault
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

@DisableCachingByDefault(because = "Not worth caching")
internal abstract class PrepareVectorDrawables : DefaultTask() {

    companion object {
        const val TASK_NAME = "prepareVectorDrawables"
        const val TASK_DESCRIPTION = "Copy VectorDrawable XML files to the destination directory."

        private const val DRAWABLE_DIR = "drawable"
        private const val XML_PATTERN = "*.xml"
        private const val XML_EXT = ".xml"
        private const val FILLED_SUFFIX = "_filled"
        private const val OUTLINED_SUFFIX = "_outlined"

        private val File.nameWithoutExt: String
            get() = name.removeSuffix(XML_EXT)
    }

    private enum class Style {
        FILLED, OUTLINED;

        val styleName: String
            get() = name.lowercase()
    }

    @get:Input
    abstract val projectType: Property<ProjectType>

    @get:InputDirectory
    @get:SkipWhenEmpty
    @get:IgnoreEmptyDirectories
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val srcDir: DirectoryProperty

    @get:Input
    abstract val clearDstDir: Property<Boolean>

    @get:OutputDirectory
    abstract val dstDir: DirectoryProperty

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    private fun Directory.clear() {
        val file = this.asFile
        file.walkBottomUp().forEach { if (it != file) it.delete() }
    }

    private fun Directory.createStyleSubDirs() {
        Style.entries.forEach { style ->
            dir(style.styleName).asFile.mkdir()
        }
    }

    private fun WorkQueue.copy(
        dstDir: Directory,
        file: File,
        resolvedName: String,
        style: Style? = null
    ) {
        val fileDstDir = if (style != null) {
            dstDir.dir(style.styleName)
        } else {
            dstDir
        }
        val dst = fileDstDir.file(resolvedName + XML_EXT).asFile
        submit(CopyFileAction::class) {
            srcFile = file
            dstFile = dst
        }
    }

    @TaskAction
    fun execute() {
        val projectType: ProjectType = projectType.get()
        val dstDir = dstDir.get().let {
            if (clearDstDir.get()) {
                it.clear()
            }
            when (projectType) {
                ProjectType.RES -> {
                    it.dir(DRAWABLE_DIR).apply {
                        asFile.mkdir()
                    }
                }
                ProjectType.COMPOSE -> {
                    it.apply {
                        createStyleSubDirs()
                    }
                }
            }
        }

        val files = srcDir.asFileTree.matching { include(XML_PATTERN) }.files.sorted()
        val fileSet = HashMap<String, File>(files.size).apply {
            files.associateByTo(this) { it.nameWithoutExt }
        }
        val workQueue = workerExecutor.noIsolation()
        var count = 0
        for (file in files) {
            val name = file.nameWithoutExt
            if (name !in fileSet) continue
            var resolvedName = name
            var bothStylesExist = false

            // Filled always comes before outlined after sorting
            if (name.endsWith(FILLED_SUFFIX)) {
                resolvedName = name.removeSuffix(FILLED_SUFFIX)
                val outlined = resolvedName + OUTLINED_SUFFIX
                val outlinedFile = fileSet[outlined]
                if (outlinedFile != null) {
                    // Both filled and outlined exist
                    when (projectType) {
                        ProjectType.RES -> {
                            workQueue.copy(dstDir, outlinedFile, outlined, null)
                        }
                        ProjectType.COMPOSE -> {
                            workQueue.copy(dstDir, outlinedFile, resolvedName, Style.OUTLINED)
                        }
                    }
                    bothStylesExist = true
                    fileSet.remove(outlined)
                }
            } else if (name.endsWith(OUTLINED_SUFFIX)) {
                // Only outlined exists
                resolvedName = name.removeSuffix(OUTLINED_SUFFIX)
            }

            when (projectType) {
                ProjectType.RES -> {
                    workQueue.copy(
                        dstDir, file,
                        if (bothStylesExist) resolvedName + FILLED_SUFFIX else resolvedName,
                        null
                    )
                    count += if (bothStylesExist) 2 else 1
                }
                ProjectType.COMPOSE -> {
                    workQueue.copy(dstDir, file, resolvedName, Style.FILLED)
                    if (!bothStylesExist) {
                        workQueue.copy(dstDir, file, resolvedName, Style.OUTLINED)
                    }
                    count++
                }
            }
            fileSet.remove(name)
        }

        logger.info("Added {} icons for {}.", count, projectType.name.lowercase())
    }
}
