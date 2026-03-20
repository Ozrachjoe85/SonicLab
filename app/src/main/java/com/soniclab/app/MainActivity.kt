package com.soniclab.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.soniclab.app.ui.screens.NowPlayingScreen
import com.soniclab.app.ui.screens.PlaceholderScreen
import com.soniclab.app.ui.theme.SonicLabTheme
import com.soniclab.app.ui.theme.sonicColors
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity with Bottom Navigation
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SonicLabTheme {
                MainScreen()
            }
        }
    }
}

/**
 * Navigation destinations
 */
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object NowPlaying : Screen("now_playing", "Now Playing", Icons.Default.PlayArrow)
    object Collection : Screen("collection", "Collection", Icons.Default.LibraryMusic)
    object Visuals : Screen("visuals", "Visuals", Icons.Default.GraphicEq)
    object Lab : Screen("lab", "Lab", Icons.Default.Settings)
}

/**
 * Main screen with bottom navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val colors = sonicColors
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.NowPlaying) }
    
    val screens = listOf(
        Screen.NowPlaying,
        Screen.Collection,
        Screen.Visuals,
        Screen.Lab
    )
    
    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            NavigationBar(
                containerColor = colors.surface,
                contentColor = colors.textPrimary
            ) {
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                screen.icon, 
                                contentDescription = screen.title,
                                tint = if (selectedScreen == screen) colors.primary else colors.textSecondary
                            )
                        },
                        label = { 
                            Text(
                                screen.title,
                                color = if (selectedScreen == screen) colors.primary else colors.textSecondary
                            )
                        },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colors.primary,
                            selectedTextColor = colors.primary,
                            unselectedIconColor = colors.textSecondary,
                            unselectedTextColor = colors.textSecondary,
                            indicatorColor = colors.primary.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        // Content with proper padding to avoid bottom nav overlap
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                Screen.NowPlaying -> NowPlayingScreen()
                Screen.Collection -> PlaceholderScreen("Collection", "Your music library will appear here")
                Screen.Visuals -> PlaceholderScreen("Visuals", "Audio visualizations coming soon")
                Screen.Lab -> PlaceholderScreen("Lab", "EQ and audio controls coming soon")
            }
        }
    }
}
