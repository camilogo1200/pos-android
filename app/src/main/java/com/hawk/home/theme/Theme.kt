package com.hawk.home.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = HawkSurfaceMuted,
    onPrimary = HawkText,
    secondary = HawkShowcaseAccent,
    onSecondary = HawkText,
    background = HawkText,
    onBackground = HawkBackground,
    surface = HawkPrimaryPressed,
    onSurface = HawkBackground,
    outline = HawkSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = HawkPrimary,
    onPrimary = HawkSurface,
    secondary = HawkSecondary,
    onSecondary = HawkSurface,
    background = HawkBackground,
    onBackground = HawkText,
    surface = HawkSurface,
    onSurface = HawkText,
    outline = HawkBorder
)

@Composable
fun HawkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
