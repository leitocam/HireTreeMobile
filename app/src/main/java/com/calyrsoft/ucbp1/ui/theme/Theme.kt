package com.calyrsoft.ucbp1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = iOSBlue,
    onPrimary = Color.White,
    primaryContainer = iOSIndigo,
    onPrimaryContainer = Color.White,

    secondary = iOSTeal,
    onSecondary = Color.White,
    secondaryContainer = iOSPurple,
    onSecondaryContainer = Color.White,

    tertiary = iOSPink,
    onTertiary = Color.White,

    background = iOSBackgroundDark,
    onBackground = iOSLabelDark,

    surface = iOSSecondaryBackgroundDark,
    onSurface = iOSLabelDark,
    surfaceVariant = CardBackgroundDark,
    onSurfaceVariant = iOSSecondaryLabelDark,

    error = iOSRed,
    onError = Color.White,

    outline = iOSSystemGray,
    outlineVariant = iOSSystemGray3
)

private val LightColorScheme = lightColorScheme(
    primary = iOSBlue,
    onPrimary = Color.White,
    primaryContainer = iOSSystemGray6,
    onPrimaryContainer = iOSBlue,

    secondary = iOSTeal,
    onSecondary = Color.White,
    secondaryContainer = iOSSystemGray5,
    onSecondaryContainer = iOSTeal,

    tertiary = iOSPink,
    onTertiary = Color.White,
    tertiaryContainer = iOSSystemGray6,
    onTertiaryContainer = iOSPink,

    background = iOSBackgroundLight,
    onBackground = iOSLabelLight,

    surface = CardBackgroundLight,
    onSurface = iOSLabelLight,
    surfaceVariant = iOSSecondaryBackgroundLight,
    onSurfaceVariant = iOSSecondaryLabelLight,

    error = iOSRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = iOSSystemGray3,
    outlineVariant = iOSSystemGray5,

    inverseSurface = iOSLabelLight,
    inverseOnSurface = Color.White
)

@Composable
fun Ucbp1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use iOS-inspired colors
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}