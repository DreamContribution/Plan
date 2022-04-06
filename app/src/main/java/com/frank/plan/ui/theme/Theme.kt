package com.frank.plan.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color


private val LightColorPalette = PlanColors(
    titleBg = Purple700,
    mainBg = white,
    titleTextColor = white,
    textColor = black,
    iconTint = black,
    separate = gray,
    iconTintSelected = red,
)

private val DarkColorPalette = PlanColors(
    titleBg = red,
    mainBg = red,
    titleTextColor = white,
    textColor = white,
    iconTint = white,
    separate = white,
    iconTintSelected = black,
)

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
    titleBg: Color,
    textColor: Color,
    iconTint: Color,
    separate: Color,
    iconTintSelected: Color,
    mainBg: Color,
    titleTextColor: Color
) {
    var titleBg: Color by mutableStateOf(titleBg)
        private set
    var textColor: Color by mutableStateOf(textColor)
        private set
    var iconTint: Color by mutableStateOf(iconTint)
        private set
    var separate: Color by mutableStateOf(separate)
        private set
    var iconTintSelected by mutableStateOf(iconTintSelected)
        private set
    var mainBg by mutableStateOf(mainBg)
        private set
    var titleTextColor by mutableStateOf(titleTextColor)
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

    val titleBg = animateColorAsState(targetColors.titleBg, TweenSpec(600))
    val textColor = animateColorAsState(targetColors.textColor, TweenSpec(600))
    val iconTint = animateColorAsState(targetColors.iconTint, TweenSpec(600))
    val separate = animateColorAsState(targetColors.separate, TweenSpec(600))
    val iconTintSelected = animateColorAsState(targetColors.iconTintSelected, TweenSpec(600))
    val mainBg = animateColorAsState(targetColors.mainBg, TweenSpec(600))
    val titleTextColor = animateColorAsState(targetColors.titleTextColor, TweenSpec(600))
    val colors = PlanColors(
        titleBg = titleBg.value,
        textColor = textColor.value,
        iconTint = iconTint.value,
        separate = separate.value,
        iconTintSelected = iconTintSelected.value,
        mainBg = mainBg.value,
        titleTextColor = titleTextColor.value
    )

    CompositionLocalProvider(LocalPlanColors provides colors) {
        MaterialTheme(
            shapes = shapes,
            content = content
        )
    }

}