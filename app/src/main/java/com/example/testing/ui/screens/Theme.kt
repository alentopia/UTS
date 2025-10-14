package com.example.testing.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF8B4CFC),
    secondary = Color(0xFFD1E5FF),
    background = Color(0xFFF8F8FF),
    onPrimary = Color.White,
    onBackground = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBBA8FF),
    secondary = Color(0xFF44415A),
    background = Color(0xFF121212),
    onPrimary = Color.White,
    onBackground = Color(0xFFEAEAEA)
)

@Composable
fun AppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}
