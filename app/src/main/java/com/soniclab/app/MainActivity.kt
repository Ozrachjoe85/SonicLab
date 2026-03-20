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
import com.soniclab.app.ui.screens.CollectionScreen
import com.soniclab.app.ui.screens.LabScreen
import com.soniclab.app.ui.screens.NowPlayingScreen
import com.soniclab.app.ui.screens.VisualsScreen
import com.soniclab.app.ui.theme.SonicLabTheme
import com.soniclab.app.ui.theme.sonicColors
import com.soniclab.app.util.PermissionHandler
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity - FULLY WIRED with ALL working screens
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private lateinit var permissionHandler: PermissionHandler
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        permissionHandler = PermissionHandler(this) { granted ->
            // Permissions result - UI will react via ViewModel
        }
        
        if (!permissionHandler.hasPermissions(this)) {
            permissionHandler.requestPermissions()
        }
        
        setContent {
            SonicLabTheme {
                MainScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object NowPlaying : Screen("now_playing", "Now Playing", Icons.Default.PlayArrow)
    object Collection : Screen("collection", "Collection", Icons.Default.LibraryMusic)
    object Visuals : Screen("visuals", "Visuals", Icons.Default.GraphicEq)
    object Lab : Screen("lab", "Lab", Icons.Default.Settings)
}

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
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                Screen.NowPlaying -> NowPlayingScreen()
                Screen.Collection -> CollectionScreen()
                Screen.Visuals -> VisualsScreen()
                Screen.Lab -> LabScreen()
            }
        }
    }
}
