package com.pombiss.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun PomBissTheme(
    primaryColor: Color = NeonBlue,
    accentColor: Color = NeonGreen,
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme(
        primary = primaryColor,
        secondary = accentColor,
        tertiary = NeonPink,
        background = DarkBackground,
        surface = SurfaceDark,
        onPrimary = TextWhite,
        onSecondary = Color.Black,
        onTertiary = TextWhite,
        onBackground = TextWhite,
        onSurface = TextWhite,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
