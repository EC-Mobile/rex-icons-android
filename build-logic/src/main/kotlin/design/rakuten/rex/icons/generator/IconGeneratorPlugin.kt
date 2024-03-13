/*
 * Copyright 2020 The Android Open Source Project
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

package design.rakuten.rex.icons.generator

import design.rakuten.rex.icons.svgconverter.PrepareVectorDrawables
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.utils.named

class IconGeneratorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val task = target.registerTask()
        target.registerSrcDir(task)
    }
}

private fun Project.registerTask() =
    tasks.register<IconGenerationTask>(IconGenerationTask.TASK_NAME) {
        description = IconGenerationTask.TASK_DESCRIPTION
        val prepareTask = tasks.named<PrepareVectorDrawables>(PrepareVectorDrawables.TASK_NAME)
        allIconsDirectory
            .value(prepareTask.flatMap { it.dstDir })
            .disallowChanges()
        expectedApiFile
            .value(layout.projectDirectory.file(IconGenerationTask.API_FILE_PATH))
            .disallowChanges()
        buildDirectory
            .value(layout.buildDirectory)
            .disallowChanges()
    }

private fun Project.registerSrcDir(
    task: TaskProvider<IconGenerationTask>
) {
    val sourceSet = getMultiplatformSourceSet(KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME)
    val generatedSrcMainDirectory = task.map { it.generatedSrcMainDirectory }
    sourceSet.kotlin.srcDir(files(generatedSrcMainDirectory).builtBy(task))
}

private fun Project.getMultiplatformSourceSet(name: String): KotlinSourceSet {
    val sourceSet = project.extensions
        .getByType<KotlinMultiplatformExtension>()
        .sourceSets
        .findByName(name)
    return requireNotNull(sourceSet) {
        "No source sets found matching $name"
    }
}
