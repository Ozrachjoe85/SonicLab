package com.soniclab.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// Available themes
enum class AppTheme {
    CYBER_DIGITAL_GHOST,
    PURE_ANALOG_WARMTH,
    TAPEDECK_RETRO,
    WALKMAN_PORTABLE,
    CAR_MODE,
    AUDIOPHILE_PRO,
    CUSTOM_MODULAR
}

// Theme colors interface
data class ThemeColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val vuLow: Color,
    val vuMid: Color,
    val vuHigh: Color,
    val vuPeak: Color,
    val glow: Color,
    val border: Color
)

// Theme-specific color schemes
object ThemeColorSchemes {
    val CyberDigitalGhost = ThemeColors(
        background = CosmicVoid,
        surface = BgPanel,
        primary = NeonCyan,
        secondary = PlasmaViolet,
        accent = NeonPink,
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        vuLow = VUCosmicLow,
        vuMid = VUCosmicMid,
        vuHigh = VUCosmicHigh,
        vuPeak = VUCosmicPeak,
        glow = NeonCyan,
        border = GridCyan
    )
    
    val PureAnalogWarmth = ThemeColors(
        background = Color(0xFF2C1810), // WalnutDark
        surface = Color(0xFF4A2C1B), // WalnutMid
        primary = Color(0xFFFF9955), // TubeGlow
        secondary = Color(0xFFFFD700), // Gold
        accent = Color(0xFFFF6E40), // NixieTubeOrange
        textPrimary = Color(0xFFF5F5F5), // LabelWhite
        textSecondary = Color(0xFFBDBDBD), // LabelGray
        vuLow = Color(0xFF4CAF50), // VUGreen
        vuMid = Color(0xFFFFB300), // VUAmber
        vuHigh = Color(0xFFE53935), // VURed
        vuPeak = Color(0xFFFFFFFF),
        glow = Color(0xFFFF8A50),
        border = Color(0xFFD4AF37) // EngravingGold
    )
    
    val TapedeckRetro = ThemeColors(
        background = Color(0xFF1A1A1A),
        surface = Color(0xFF2D2D2D),
        primary = Color(0xFFC0C0C0), // Silver
        secondary = Color(0xFF8B4513), // Brown
        accent = Color(0xFFFF0000), // Red button
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFFAAAAAA),
        vuLow = Color(0xFF00FF00),
        vuMid = Color(0xFFFFFF00),
        vuHigh = Color(0xFFFF0000),
        vuPeak = Color(0xFFFFFFFF),
        glow = Color(0xFFFF6666),
        border = Color(0xFF666666)
    )
    
    val WalkmanPortable = ThemeColors(
        background = Color(0xFF0D1B2A), // Navy blue
        surface = Color(0xFF1B263B),
        primary = Color(0xFF4169E1), // Royal blue
        secondary = Color(0xFFFFD700), // Gold accent
        accent = Color(0xFFFF6B35), // Orange
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFFB0C4DE),
        vuLow = Color(0xFF00D9FF),
        vuMid = Color(0xFFFFAA00),
        vuHigh = Color(0xFFFF3366),
        vuPeak = Color(0xFFFFFFFF),
        glow = Color(0xFF4169E1),
        border = Color(0xFF4169E1)
    )
    
    val CarMode = ThemeColors(
        background = Color(0xFF000000),
        surface = Color(0xFF1A1A1A),
        primary = Color(0xFFFF0000), // High contrast red
        secondary = Color(0xFFFFFFFF),
        accent = Color(0xFF00FF00), // Green
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFFCCCCCC),
        vuLow = Color(0xFF00FF00),
        vuMid = Color(0xFFFFFF00),
        vuHigh = Color(0xFFFF0000),
        vuPeak = Color(0xFFFFFFFF),
        glow = Color(0xFFFF0000),
        border = Color(0xFFFFFFFF)
    )
    
    val AudiophilePro = ThemeColors(
        background = Color(0xFF0A0A0A),
        surface = Color(0xFF1A1A1A),
        primary = Color(0xFF00FFFF), // Cyan
        secondary = Color(0xFF333333),
        accent = Color(0xFF00FFFF),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFF888888),
        vuLow = Color(0xFF00FFAA),
        vuMid = Color(0xFF00DDFF),
        vuHigh = Color(0xFF00AAFF),
        vuPeak = Color(0xFFFFFFFF),
        glow = Color(0xFF00FFFF),
        border = Color(0xFF333333)
    )
}

// Theme manager class
class ThemeManager {
    var currentTheme by mutableStateOf(AppTheme.CYBER_DIGITAL_GHOST)
        private set
    
    val colors: ThemeColors
        get() = when (currentTheme) {
            AppTheme.CYBER_DIGITAL_GHOST -> ThemeColorSchemes.CyberDigitalGhost
            AppTheme.PURE_ANALOG_WARMTH -> ThemeColorSchemes.PureAnalogWarmth
            AppTheme.TAPEDECK_RETRO -> ThemeColorSchemes.TapedeckRetro
            AppTheme.WALKMAN_PORTABLE -> ThemeColorSchemes.WalkmanPortable
            AppTheme.CAR_MODE -> ThemeColorSchemes.CarMode
            AppTheme.AUDIOPHILE_PRO -> ThemeColorSchemes.AudiophilePro
            AppTheme.CUSTOM_MODULAR -> ThemeColorSchemes.CyberDigitalGhost // Default for now
        }
    
    fun setTheme(theme: AppTheme) {
        currentTheme = theme
    }
}

// Composition local for theme manager
val LocalThemeManager = compositionLocalOf { ThemeManager() }

@Composable
fun rememberThemeManager(): ThemeManager {
    return remember { ThemeManager() }
}
