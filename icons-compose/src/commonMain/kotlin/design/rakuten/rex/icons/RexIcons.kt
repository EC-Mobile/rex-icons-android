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

package design.rakuten.rex.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import design.rakuten.rex.icons.RexIcons.Filled
import design.rakuten.rex.icons.RexIcons.Outlined

/**
 * [ReX icons](https://rex.rakuten.design/en/design/the-basics/icons/).
 *
 * There are two distinct icon themes: [Filled] and [Outlined]. Each theme contains the same icons,
 * but with a distinct visual style.
 *
 * Icons mostly maintain the same names defined by ReX, but with their name converted to PascalCase.
 * For example: "Arrow left" becomes ArrowLeft.
 */
object RexIcons {
    /**
     * Filled icons are the default icon theme. You can also use [Default] as an alias for these
     * icons.
     */
    object Filled

    /**
     * Outlined icons make use of a thin stroke and empty space inside for a lighter appearance.
     */
    object Outlined

    /**
     * Alias for [Filled], the baseline icon theme.
     */
    val Default = Filled
}

/**
 * Utility delegate to construct a ReX icon with default size information.
 * This is used by generated icons, and should not be used manually.
 *
 * @param name the full name of the generated icon
 * @param autoMirror determines if the vector asset should automatically be mirrored for right to
 * left locales
 * @param block builder lambda to add paths to this vector asset
 */
internal inline fun rexIcon(
    name: String,
    autoMirror: Boolean = false,
    block: ImageVector.Builder.() -> ImageVector.Builder
): ImageVector = ImageVector.Builder(
    name = name,
    defaultWidth = IconDefaultSize.dp,
    defaultHeight = IconDefaultSize.dp,
    viewportWidth = IconViewportSize,
    viewportHeight = IconViewportSize,
    autoMirror = autoMirror
).block().build()

/**
 * Adds a vector path to this icon with ReX defaults.
 *
 * @param fillAlpha fill alpha for this path
 * @param strokeAlpha stroke alpha for this path
 * @param pathFillType [PathFillType] for this path
 * @param pathBuilder builder lambda to add commands to this path
 */
internal inline fun ImageVector.Builder.rexPath(
    fillAlpha: Float = 1f,
    strokeAlpha: Float = 1f,
    pathFillType: PathFillType = DefaultFillType,
    pathBuilder: PathBuilder.() -> Unit
) = path(
    fill = SolidColor(Color.Black),
    fillAlpha = fillAlpha,
    stroke = null,
    strokeAlpha = strokeAlpha,
    strokeLineWidth = 1f,
    strokeLineCap = StrokeCap.Butt,
    strokeLineJoin = StrokeJoin.Bevel,
    strokeLineMiter = 1f,
    pathFillType = pathFillType,
    pathBuilder = pathBuilder
)

private const val IconDefaultSize = 24f

// All icons (currently) have a viewport size of 32 by 32.
private const val IconViewportSize = 32f
