package com.soniclab.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RetroColorScheme = darkColorScheme(
    // Primary - Glowing orange accents (like warm tubes)
    primary = NixieTubeOrange,
    onPrimary = WalnutDark,
    primaryContainer = WalnutMid,
    onPrimaryContainer = TubeGlowWarm,
    
    // Secondary - Blue LEDs and switches
    secondary = LEDBlue,
    onSecondary = WalnutDark,
    secondaryContainer = BrushedAluminum,
    onSecondaryContainer = LEDBlue,
    
    // Backgrounds - Rich wood tones
    background = WalnutDark,
    onBackground = LabelWhite,
    surface = WalnutMid,
    onSurface = LabelWhite,
    surfaceVariant = BrushedAluminum,
    onSurfaceVariant = WalnutDark,
    
    // Outline - Metal bezels
    outline = BrushedAluminumLight,
    outlineVariant = ChromeShadow,
    
    // Semantic colors
    error = LEDRed,
    onError = LabelWhite,
)

@Composable
fun SonicLabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = RetroColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
