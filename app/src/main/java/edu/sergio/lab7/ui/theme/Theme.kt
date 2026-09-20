package edu.sergio.lab7.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF487F99),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDFEDF3),
    background = Color(0xFFFAFBFC),
    surface = Color(0xFFFAFBFC),
    onSurface = Color(0xFF203039),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9BCDE4),
    onPrimary = Color(0xFF133544),
    background = Color(0xFF10191E),
    surface = Color(0xFF10191E),
)

@Composable
fun LabTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content,
    )
}
