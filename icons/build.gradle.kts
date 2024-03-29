import com.vanniktech.maven.publish.AndroidSingleVariantLibrary

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

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.svgConverter.res)
    alias(libs.plugins.publish)
}

android {
    namespace = "design.rakuten.rex.icons"
    compileSdk = libs.versions.sdk.compile.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        vectorDrawables {
            // Disable PNG generation
            generatedDensities()
        }
    }
}

mavenPublishing {
    configure(AndroidSingleVariantLibrary())
    pomFromGradleProperties()
    coordinates(artifactId = "icons")
    pom {
        name = "ReX Icons"
        description = name
    }
}
