package com.sachin.core_design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Brand70,
    onPrimary = Neutral10,
    primaryContainer = Neutral10,
    onPrimaryContainer = Neutral10,
    secondary = Success70,
    onSecondary = Neutral10,
    secondaryContainer = Success70,
    onSecondaryContainer = Neutral10,
    tertiary = Warning70,
    onTertiary = Neutral10,
    tertiaryContainer = Warning70,
    onTertiaryContainer = Neutral10,
    background = Neutral10,
    onBackground = Neutral100,
    surface = Neutral10,
    onSurface = Neutral100,
    error = Error70,
    errorContainer = Error70,
    onErrorContainer = Neutral10,
    outline = Brand70,
)

private val LightColorScheme = lightColorScheme(
    primary = Brand50,
    onPrimary = Neutral100,
    primaryContainer = Brand30,
    onPrimaryContainer = Neutral100,
    secondary = Success50,
    onSecondary = Neutral10,
    secondaryContainer = Success50,
    onSecondaryContainer = Neutral10,
    tertiary = Warning50,
    onTertiary = Neutral10,
    tertiaryContainer = Warning50,
    onTertiaryContainer = Neutral10,
    background = Neutral100,
    onBackground = Neutral10,
    surface = Neutral100,
    onSurface = Neutral10,
    error = Error50,
    errorContainer = Error50,
    onErrorContainer = Neutral100,
    outline = Brand50,
)

@Composable
fun PopulationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

object PopulationTheme {
    val colorScheme: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes
}