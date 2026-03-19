package com.soniclab.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NixieTubeOrange,
    onPrimary = WalnutDark,
    primaryContainer = WalnutMid,
    onPrimaryContainer = TubeGlowWarm,
    
    secondary = LEDBlue,
    onSecondary = WalnutDark,
    secondaryContainer = BrushedAluminum,
    onSecondaryContainer = LEDBlue,
    
    background = WalnutDark,
    onBackground = LabelWhite,
    surface = WalnutMid,
    onSurface = LabelWhite,
    surfaceVariant = BrushedAluminum,
    onSurfaceVariant = WalnutDark,
    
    outline = BrushedAluminumLight,
    outlineVariant = ChromeShadow,
    
    error = LEDRed,
    onError = LabelWhite
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Purple80,
    onPrimaryContainer = Purple40,
    
    secondary = PurpleGrey40,
    onSecondary = Color.White,
    secondaryContainer = PurpleGrey80,
    onSecondaryContainer = PurpleGrey40,
    
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = PurpleGrey80,
    onSurfaceVariant = PurpleGrey40,
    
    outline = PurpleGrey40,
    outlineVariant = PurpleGrey80,
    
    error = Color.Red,
    onError = Color.White
)

@Composable
fun SonicLabTheme(
    darkTheme: Boolean = true, // Always use dark theme for retro aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
