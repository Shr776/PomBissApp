package com.pombiss.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PomBissColorScheme = darkColorScheme(
    primary = NeonBlue,
    secondary = NeonGreen,
    tertiary = NeonPink,
    background = DarkBackground,
    surface = SurfaceDark,
    onPrimary = TextWhite,
    onSecondary = Color.Black,
    onTertiary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite,
)

@Composable
fun PomBissTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PomBissColorScheme,
        content = content
    )
}
