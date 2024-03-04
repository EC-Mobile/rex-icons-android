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
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.submit
import org.gradle.workers.WorkerExecutor
import javax.inject.Inject

internal abstract class ConvertSvgFiles : DefaultTask() {

    companion object {
        const val TASK_NAME = "convertSvg"
        const val TASK_DESCRIPTION = "Convert SVG files to VectorDrawable XML."

        private const val SVG_PATTERN = "*.svg"
    }

    @get:InputDirectory
    @get:SkipWhenEmpty
    @get:IgnoreEmptyDirectories
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val srcDir: DirectoryProperty

    @get:OutputDirectory
    abstract val dstDir: DirectoryProperty

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    @TaskAction
    fun execute() {
        val src = srcDir.asFileTree.matching {
            include(SVG_PATTERN)
        }
        val isEmpty = src.isEmpty
        if (isEmpty) {
            logger.info("No SVG files found.")
        } else {
            val workQueue = workerExecutor.noIsolation()
            var count = 0
            src.visit {
                count++
                workQueue.submit(ConvertSvgAction::class) {
                    srcFile = file
                    dstDir = this@ConvertSvgFiles.dstDir
                }
            }
            logger.info("Found {} SVG files.", count)
        }
        didWork = !isEmpty
    }
}
