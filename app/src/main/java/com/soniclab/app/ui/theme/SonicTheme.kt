package com.soniclab.app.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

/**
 * Available theme presets
 */
enum class SonicTheme {
    VOID_LAB,           // Default: Cyber-lab cyan/magenta
    NEON_HORIZON,       // Synthwave pink/orange
    STUDIO_MONO,        // Grayscale + accent
    EMERALD_GRID,       // Green matrix
    CRIMSON_PULSE,      // Red + black
    ICE_CIRCUIT,        // Blue/white
    CUSTOM              // User-created
}

/**
 * Complete theme color system
 */
data class SonicColors(
    // Base colors
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    
    // Accent colors
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    
    // Text colors
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    
    // VU Meter colors
    val vuLow: Color,
    val vuMid: Color,
    val vuHigh: Color,
    val vuPeak: Color,
    
    // Functional colors
    val success: Color,
    val warning: Color,
    val error: Color,
    
    // Effect colors
    val glow: Color,
    val border: Color,
    val overlay: Color
)

/**
 * Theme visual effects configuration
 */
data class ThemeEffects(
    val glowRadius: Float = 24f,       // 0-48px
    val bloomStrength: Float = 0.4f,   // 0-1.0
    val animationSpeed: Float = 1.0f,  // 0.4-1.8×
    val showScanlines: Boolean = false,
    val showCRTCurvature: Boolean = false,
    val showHolographicRim: Boolean = true,
    val showParticleTrails: Boolean = true
)

/**
 * Complete theme definition
 */
data class SonicThemeData(
    val theme: SonicTheme,
    val colors: SonicColors,
    val effects: ThemeEffects = ThemeEffects(),
    val isDark: Boolean = true
)

/**
 * Void Lab - Default cyber-lab theme
 */
val VoidLabTheme = SonicThemeData(
    theme = SonicTheme.VOID_LAB,
    colors = SonicColors(
        // Base
        background = Color(0xFF000000),        // Pure OLED black
        surface = Color(0xFF0A0A0F),           // Subtle panel
        surfaceVariant = Color(0xFF151520),    // Lighter panel
        
        // Accents
        primary = Color(0xFF00F0FF),           // Electric cyan
        secondary = Color(0xFFFF00AA),         // Magenta
        tertiary = Color(0xFFCC00FF),          // Purple
        
        // Text
        textPrimary = Color(0xFFF0F0FF),       // Warm white
        textSecondary = Color(0xFFE0E0FF),     // Off-white
        textTertiary = Color(0xFFB0B0C0),      // Dimmed
        
        // VU Meters
        vuLow = Color(0xFF4CAF50),             // Green
        vuMid = Color(0xFFFFB300),             // Amber
        vuHigh = Color(0xFFE53935),            // Red
        vuPeak = Color(0xFFFFFFFF),            // White
        
        // Functional
        success = Color(0xFF4CAF50),
        warning = Color(0xFFFFB300),
        error = Color(0xFFE53935),
        
        // Effects
        glow = Color(0xFF00F0FF),              // Cyan glow
        border = Color(0xFF2A2A3F),
        overlay = Color(0x80000000)            // 50% black
    ),
    effects = ThemeEffects(
        glowRadius = 24f,
        bloomStrength = 0.4f,
        animationSpeed = 1.0f,
        showScanlines = false,
        showCRTCurvature = false,
        showHolographicRim = true,
        showParticleTrails = true
    )
)

/**
 * Neon Horizon - Synthwave aesthet ic
 */
val NeonHorizonTheme = VoidLabTheme.copy(
    theme = SonicTheme.NEON_HORIZON,
    colors = VoidLabTheme.colors.copy(
        primary = Color(0xFFFF1B8D),           // Hot pink
        secondary = Color(0xFFFF6B35),         // Orange
        tertiary = Color(0xFFFFD700),          // Gold
        glow = Color(0xFFFF1B8D)
    ),
    effects = VoidLabTheme.effects.copy(
        glowRadius = 32f,
        bloomStrength = 0.6f
    )
)

/**
 * Studio Mono - Clinical grayscale
 */
val StudioMonoTheme = VoidLabTheme.copy(
    theme = SonicTheme.STUDIO_MONO,
    colors = VoidLabTheme.colors.copy(
        primary = Color(0xFFFFFFFF),           // White accent
        secondary = Color(0xFF808080),         // Gray
        tertiary = Color(0xFF404040),          // Dark gray
        glow = Color(0xFFFFFFFF)
    ),
    effects = VoidLabTheme.effects.copy(
        glowRadius = 16f,
        bloomStrength = 0.2f,
        showHolographicRim = false
    )
)

/**
 * Emerald Grid - Matrix green
 */
val EmeraldGridTheme = VoidLabTheme.copy(
    theme = SonicTheme.EMERALD_GRID,
    colors = VoidLabTheme.colors.copy(
        primary = Color(0xFF00FF41),           // Matrix green
        secondary = Color(0xFF00CC33),         // Dark green
        tertiary = Color(0xFF008F11),          // Forest green
        glow = Color(0xFF00FF41)
    ),
    effects = VoidLabTheme.effects.copy(
        glowRadius = 28f,
        showScanlines = true
    )
)

/**
 * Crimson Pulse - Aggressive red
 */
val CrimsonPulseTheme = VoidLabTheme.copy(
    theme = SonicTheme.CRIMSON_PULSE,
    colors = VoidLabTheme.colors.copy(
        primary = Color(0xFFFF0000),           // Pure red
        secondary = Color(0xFFCC0000),         // Dark red
        tertiary = Color(0xFF880000),          // Blood red
        glow = Color(0xFFFF0000)
    ),
    effects = VoidLabTheme.effects.copy(
        glowRadius = 36f,
        bloomStrength = 0.7f,
        animationSpeed = 1.4f
    )
)

/**
 * Ice Circuit - Cool minimal
 */
val IceCircuitTheme = VoidLabTheme.copy(
    theme = SonicTheme.ICE_CIRCUIT,
    colors = VoidLabTheme.colors.copy(
        background = Color(0xFF000510),        // Deep blue-black
        primary = Color(0xFF00D9FF),           // Ice blue
        secondary = Color(0xFF4FC3F7),         // Sky blue
        tertiary = Color(0xFFFFFFFF),          // White
        glow = Color(0xFF00D9FF)
    ),
    effects = VoidLabTheme.effects.copy(
        glowRadius = 20f,
        bloomStrength = 0.3f,
        animationSpeed = 0.8f
    )
)

/**
 * Theme manager - holds current theme state
 */
class ThemeManager {
    var currentTheme by mutableStateOf(VoidLabTheme)
        private set
    
    fun setTheme(theme: SonicTheme) {
        currentTheme = when (theme) {
            SonicTheme.VOID_LAB -> VoidLabTheme
            SonicTheme.NEON_HORIZON -> NeonHorizonTheme
            SonicTheme.STUDIO_MONO -> StudioMonoTheme
            SonicTheme.EMERALD_GRID -> EmeraldGridTheme
            SonicTheme.CRIMSON_PULSE -> CrimsonPulseTheme
            SonicTheme.ICE_CIRCUIT -> IceCircuitTheme
            SonicTheme.CUSTOM -> currentTheme // Keep custom if set
        }
    }
    
    fun setCustomTheme(themeData: SonicThemeData) {
        currentTheme = themeData.copy(theme = SonicTheme.CUSTOM)
    }
    
    fun updateEffects(effects: ThemeEffects) {
        currentTheme = currentTheme.copy(effects = effects)
    }
}

/**
 * Composition local for theme access
 */
val LocalThemeManager = compositionLocalOf { ThemeManager() }
val LocalSonicTheme = compositionLocalOf { VoidLabTheme }

/**
 * Remember theme manager instance
 */
@Composable
fun rememberThemeManager(): ThemeManager {
    return remember { ThemeManager() }
}

/**
 * SonicLab theme wrapper
 * Provides theme to entire app
 */
@Composable
fun SonicLabTheme(
    themeManager: ThemeManager = rememberThemeManager(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalThemeManager provides themeManager,
        LocalSonicTheme provides themeManager.currentTheme
    ) {
        content()
    }
}

/**
 * Quick access to current theme colors
 */
val sonicColors: SonicColors
    @Composable
    @ReadOnlyComposable
    get() = LocalSonicTheme.current.colors

/**
 * Quick access to current theme effects
 */
val sonicEffects: ThemeEffects
    @Composable
    @ReadOnlyComposable
    get() = LocalSonicTheme.current.effects
