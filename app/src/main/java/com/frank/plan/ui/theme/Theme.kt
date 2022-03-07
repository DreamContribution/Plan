package com.frank.plan.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = PlanColors(
    bottomBar = Purple200,
//    primaryVariant = Purple700,
//    secondary = Teal200
)

private val LightColorPalette = PlanColors(
    bottomBar = Purple500,
//    primaryVariant = Purple700,
//    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

//@Composable
//fun ComposePlanTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    content: @Composable() () -> Unit,
//) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
//
//    MaterialTheme(
//        colors = colors,
//        typography = Typography,
//        shapes = Shapes,
//        content = content
//    )
//}

private val LocalPlanColors = compositionLocalOf {
    LightColorPalette
}

object PlanTheme {
    val colors: PlanColors
        @Composable
        get() = LocalPlanColors.current

    enum class Theme {
        Light, Dark, NewYear
    }
}

@Stable
class PlanColors(
    bottomBar: Color,
) {
    var bottomBar: Color by mutableStateOf(bottomBar)
        private set
}

@Composable
fun PlanTheme(theme: PlanTheme.Theme = PlanTheme.Theme.Light, content: @Composable () -> Unit) {
    val targetColors = when (theme) {
        PlanTheme.Theme.Light -> LightColorPalette
        PlanTheme.Theme.Dark -> DarkColorPalette
        PlanTheme.Theme.NewYear -> DarkColorPalette
        else -> {
            DarkColorPalette
        }
    }

    val bottomBar = animateColorAsState(targetColors.bottomBar, TweenSpec(600))
    val colors = PlanColors(
        bottomBar = bottomBar.value
    )

    CompositionLocalProvider(LocalPlanColors provides colors) {
        MaterialTheme(
            shapes = shapes,
            content = content
        )
    }

}