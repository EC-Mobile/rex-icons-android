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

import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

internal object SvgConverterConstants {
    const val EXTENSION_NAME = "svgConverter"
    const val DEFAULT_SVG_DIR = "svg"
    const val DEFAULT_VECTOR_DRAWABLE_DIR = "vector-drawable"
    const val DEFAULT_RAW_ICONS_DIR = "raw-icons"
}

internal fun Project.registerPrepareVectorDrawablesTask(
    type: ProjectType,
    configure: PrepareVectorDrawables.() -> Unit = {}
): TaskProvider<PrepareVectorDrawables> {
    check(rootProject.plugins.hasPlugin(SvgConverterRootPlugin::class)) {
        "SVG converter plugin must also be applied to the root project."
    }
    val convertTask = rootProject.tasks.named<ConvertSvgFiles>(ConvertSvgFiles.TASK_NAME)

    return tasks.register<PrepareVectorDrawables>(PrepareVectorDrawables.TASK_NAME) {
        description = PrepareVectorDrawables.TASK_DESCRIPTION
        projectType.value(type).disallowChanges()
        srcDir.value(convertTask.flatMap { it.dstDir }).disallowChanges()
        configure()
    }
}
