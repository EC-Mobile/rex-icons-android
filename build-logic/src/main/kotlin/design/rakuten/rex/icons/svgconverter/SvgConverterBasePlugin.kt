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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

abstract class SvgConverterBasePlugin : Plugin<Project> {

    internal abstract val projectType: ProjectType

    internal abstract fun Project.configureExtension(
        ext: SvgConverterSubExtension,
        task: TaskProvider<PrepareVectorDrawables>
    )

    override fun apply(target: Project): Unit = with(target) {
        check(rootProject.plugins.hasPlugin(SvgConverterRootPlugin::class)) {
            "SVG converter plugin must also be applied to the root project."
        }
        val convertTask = rootProject.tasks.named<ConvertSvgFiles>(ConvertSvgFiles.TASK_NAME)

        val ext = extensions.create<SvgConverterSubExtension>(SvgConverterConstants.EXTENSION_NAME)
        ext.clearDstDir.convention(true)

        val task = tasks.register<PrepareVectorDrawables>(PrepareVectorDrawables.TASK_NAME) {
            description = PrepareVectorDrawables.TASK_DESCRIPTION
            projectType.value(this@SvgConverterBasePlugin.projectType).disallowChanges()
            srcDir.value(convertTask.flatMap { it.dstDir }).disallowChanges()
            clearDstDir.value(ext.clearDstDir).disallowChanges()
            dstDir.value(ext.dstDir).disallowChanges()
        }

        configureExtension(ext, task)
    }
}
