package com.frank.plan.data

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class ItemTabUIData(
    val name: String,
    var icon: ImageVector? = null,
    var iconResource: Painter? = null
)