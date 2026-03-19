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

private val DarkColorScheme = darkColorScheme(
    primary = Cyan500,
    onPrimary = Indigo900,
    primaryContainer = Indigo800,
    onPrimaryContainer = Cyan400,
    
    secondary = Amber500,
    onSecondary = Indigo900,
    secondaryContainer = Indigo700,
    onSecondaryContainer = Amber400,
    
    background = Indigo900,
    onBackground = Gray100,
    surface = Indigo800,
    onSurface = Gray100,
    surfaceVariant = Indigo700,
    onSurfaceVariant = Gray200,
    
    outline = Gray700,
    outlineVariant = Indigo600,
    
    error = Error,
    onError = Gray100,
)

@Composable
fun SonicLabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    
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
