package com.example.geekvault.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary             = CyanBlue,
    onPrimary           = CyanBlueDark,
    primaryContainer    = CyanBlueContainer,
    onPrimaryContainer  = CyanBlueOnContainer,
    secondary           = Lavender,
    onSecondary         = LavenderDark,
    secondaryContainer  = LavenderContainer,
    onSecondaryContainer = LavenderOnContainer,
    background          = NavyBackground,
    onBackground        = TextPrimary,
    surface             = NavySurface,
    onSurface           = TextPrimary,
    surfaceVariant      = NavySurfaceVariant,
    onSurfaceVariant    = TextSecondary,
    error               = ErrorRed,
    errorContainer      = ErrorRedContainer,
)

private val LightColorScheme = lightColorScheme(
    primary             = NavyLight,
    onPrimary           = SurfaceLight,
    primaryContainer    = NavyLightContainer,
    onPrimaryContainer  = NavyLightOnContainer,
    secondary           = LavenderLight,
    onSecondary         = SurfaceLight,
    secondaryContainer  = LavenderLightContainer,
    onSecondaryContainer = LavenderLightOnContainer,
    background          = BackgroundLight,
    onBackground        = TextOnLight,
    surface             = SurfaceLight,
    onSurface           = TextOnLight,
    surfaceVariant      = SurfaceVariantLight,
    onSurfaceVariant    = TextSecondaryLight,
)

/**
 * GeekVault app theme.
 *
 * Defaults to dark (navy) mode. Dynamic color adapts to the user's wallpaper on Android 12+.
 *
 * @param darkTheme Whether to apply the dark color scheme.
 * @param dynamicColor When true, uses Material You dynamic color on Android 12+.
 * @param content The composable content to theme.
 */
@Composable
fun GeekVaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}