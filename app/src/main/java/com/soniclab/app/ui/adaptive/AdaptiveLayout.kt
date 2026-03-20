package com.soniclab.app.ui.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Screen size classifications based on width
 * These determine the layout strategy for each screen
 */
sealed class ScreenSize {
    /**
     * Compact: < 600dp
     * Standard phones, Z Fold outer screen
     * Single column, bottom nav, swipe-first
     */
    object Compact : ScreenSize()
    
    /**
     * Medium: 600-840dp
     * Z Fold inner portrait, small tablets
     * Two-column possible, more breathing room
     */
    object Medium : ScreenSize()
    
    /**
     * Wide: 840-1200dp
     * Z Fold inner landscape, tablets, DeX
     * Sidebar nav, multi-panel layouts
     */
    object Wide : ScreenSize()
    
    /**
     * Extra Wide: > 1200dp
     * External monitors, DeX on display, car screens
     * Three-panel dashboard, desktop-style
     */
    object ExtraWide : ScreenSize()
}

/**
 * Orientation detection
 */
sealed class ScreenOrientation {
    object Portrait : ScreenOrientation()
    object Landscape : ScreenOrientation()
}

/**
 * Complete screen configuration info
 */
data class ScreenConfig(
    val size: ScreenSize,
    val orientation: ScreenOrientation,
    val widthDp: Dp,
    val heightDp: Dp,
    val widthPx: Int,
    val heightPx: Int,
    val densityDpi: Int
) {
    // Convenience checks
    val isCompact: Boolean get() = size is ScreenSize.Compact
    val isMedium: Boolean get() = size is ScreenSize.Medium
    val isWide: Boolean get() = size is ScreenSize.Wide
    val isExtraWide: Boolean get() = size is ScreenSize.ExtraWide
    val isPortrait: Boolean get() = orientation is ScreenOrientation.Portrait
    val isLandscape: Boolean get() = orientation is ScreenOrientation.Landscape
    
    // Layout decisions
    val shouldShowSidebar: Boolean get() = isWide || isExtraWide
    val shouldShowBottomNav: Boolean get() = isCompact || isMedium
    val canShowMultiPanel: Boolean get() = isWide || isExtraWide
    val shouldUseSingleColumn: Boolean get() = isCompact && isPortrait
}

/**
 * Composable that provides current screen configuration
 */
@Composable
fun rememberScreenConfig(): ScreenConfig {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    return remember(
        configuration.screenWidthDp,
        configuration.screenHeightDp,
        configuration.densityDpi,
        configuration.orientation
    ) {
        val widthDp = configuration.screenWidthDp.dp
        val heightDp = configuration.screenHeightDp.dp
        
        val size = when {
            configuration.screenWidthDp < 600 -> ScreenSize.Compact
            configuration.screenWidthDp < 840 -> ScreenSize.Medium
            configuration.screenWidthDp < 1200 -> ScreenSize.Wide
            else -> ScreenSize.ExtraWide
        }
        
        val orientation = if (widthDp < heightDp) {
            ScreenOrientation.Portrait
        } else {
            ScreenOrientation.Landscape
        }
        
        ScreenConfig(
            size = size,
            orientation = orientation,
            widthDp = widthDp,
            heightDp = heightDp,
            widthPx = with(density) { widthDp.toPx().toInt() },
            heightPx = with(density) { heightDp.toPx().toInt() },
            densityDpi = configuration.densityDpi
        )
    }
}

/**
 * Adaptive layout composable
 * Automatically selects the right layout based on screen size
 * 
 * Usage:
 * AdaptiveLayout(
 *     compact = { CompactNowPlayingScreen() },
 *     medium = { MediumNowPlayingScreen() },
 *     wide = { WideNowPlayingScreen() },
 *     extraWide = { ExtraWideNowPlayingScreen() }
 * )
 */
@Composable
fun AdaptiveLayout(
    compact: @Composable (ScreenConfig) -> Unit,
    medium: @Composable (ScreenConfig) -> Unit,
    wide: @Composable (ScreenConfig) -> Unit,
    extraWide: @Composable (ScreenConfig) -> Unit
) {
    val config = rememberScreenConfig()
    
    when (config.size) {
        is ScreenSize.Compact -> compact(config)
        is ScreenSize.Medium -> medium(config)
        is ScreenSize.Wide -> wide(config)
        is ScreenSize.ExtraWide -> extraWide(config)
    }
}

/**
 * Simplified adaptive layout when medium/wide can share implementation
 */
@Composable
fun AdaptiveLayout(
    compact: @Composable (ScreenConfig) -> Unit,
    expanded: @Composable (ScreenConfig) -> Unit
) {
    val config = rememberScreenConfig()
    
    when (config.size) {
        is ScreenSize.Compact -> compact(config)
        is ScreenSize.Medium,
        is ScreenSize.Wide,
        is ScreenSize.ExtraWide -> expanded(config)
    }
}

/**
 * Component-level adaptive sizing
 * Scales touch targets, spacing, etc. based on screen size
 */
object AdaptiveSizing {
    @Composable
    fun touchTarget(): Dp {
        val config = rememberScreenConfig()
        return when (config.size) {
            is ScreenSize.Compact -> 48.dp
            is ScreenSize.Medium -> 56.dp
            is ScreenSize.Wide -> 48.dp
            is ScreenSize.ExtraWide -> 48.dp
        }
    }
    
    @Composable
    fun buttonSpacing(): Dp {
        val config = rememberScreenConfig()
        return when (config.size) {
            is ScreenSize.Compact -> 16.dp
            is ScreenSize.Medium -> 20.dp
            is ScreenSize.Wide -> 24.dp
            is ScreenSize.ExtraWide -> 24.dp
        }
    }
    
    @Composable
    fun albumArtSize(): Dp {
        val config = rememberScreenConfig()
        return when {
            config.isCompact && config.isPortrait -> 280.dp
            config.isMedium && config.isPortrait -> 320.dp
            config.isWide -> 400.dp
            config.isExtraWide -> 480.dp
            else -> 320.dp
        }
    }
    
    @Composable
    fun contentPadding(): Dp {
        val config = rememberScreenConfig()
        return when (config.size) {
            is ScreenSize.Compact -> 16.dp
            is ScreenSize.Medium -> 20.dp
            is ScreenSize.Wide -> 24.dp
            is ScreenSize.ExtraWide -> 32.dp
        }
    }
}
