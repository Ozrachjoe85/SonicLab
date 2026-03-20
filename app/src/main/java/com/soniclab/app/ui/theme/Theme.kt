package com.soniclab.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CosmicDarkScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = CosmicVoid,
    primaryContainer = CosmicPurple,
    onPrimaryContainer = NeonPink,
    
    secondary = PlasmaViolet,
    onSecondary = CosmicVoid,
    secondaryContainer = CosmicBlue,
    onSecondaryContainer = NeonCyan,
    
    background = BgDeepSpace,
    onBackground = TextPrimary,
    surface = BgNebula,
    onSurface = TextPrimary,
    surfaceVariant = BgPanel,
    onSurfaceVariant = TextSecondary,
    
    outline = GridCyan,
    outlineVariant = GridPurple,
    
    error = GlitchRed,
    onError = TextPrimary
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
    onSurface = Color.Black
)

@Composable
fun SonicLabTheme(
    darkTheme: Boolean = true, // Always cosmic dark
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CosmicDarkScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
