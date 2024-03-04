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
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class SvgConverterRootPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        val ext = extensions.create<SvgConverterRootExtension>(SvgConverterConstants.EXTENSION_NAME)
        ext.srcDir.convention(
            layout.projectDirectory.dir(SvgConverterConstants.DEFAULT_SVG_DIR)
        )
        ext.dstDir.convention(
            layout.buildDirectory.dir(SvgConverterConstants.DEFAULT_VECTOR_DRAWABLE_DIR)
        )

        tasks.register<ConvertSvgFiles>(ConvertSvgFiles.TASK_NAME) {
            description = ConvertSvgFiles.TASK_DESCRIPTION
            srcDir.value(ext.srcDir).disallowChanges()
            dstDir.value(ext.dstDir).disallowChanges()
        }
    }
}
