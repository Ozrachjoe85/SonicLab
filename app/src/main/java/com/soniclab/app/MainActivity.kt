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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.soniclab.app.navigation.Screen
import com.soniclab.app.ui.screens.*
import com.soniclab.app.ui.theme.SonicLabTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            SonicLabTheme {
                SonicLabMainContent()  // ← Changed from SonicLabApp()
            }
        }
    }
}

@Composable
fun SonicLabMainContent() {  // ← Renamed from SonicLabApp
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val navItems = listOf(
                    BottomNavItem(
                        screen = Screen.HOME,
                        icon = Icons.Default.Home,
                        label = "Home"
                    ),
                    BottomNavItem(
                        screen = Screen.LIBRARY,
                        icon = Icons.Default.LibraryMusic,
                        label = "Library"
                    ),
                    BottomNavItem(
                        screen = Screen.VISUALIZER,
                        icon = Icons.Default.BarChart,
                        label = "Visualizer"
                    ),
                    BottomNavItem(
                        screen = Screen.EQUALIZER,
                        icon = Icons.Default.Equalizer,
                        label = "EQ"
                    ),
                    BottomNavItem(
                        screen = Screen.SETTINGS,
                        icon = Icons.Default.Settings,
                        label = "Settings"
                    )
                )
                
                navItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { 
                        it.route == item.screen.route 
                    } == true
                    
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selected,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HOME.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.HOME.route) { 
                HomeScreen() 
            }
            composable(Screen.LIBRARY.route) { 
                LibraryScreen() 
            }
            composable(Screen.VISUALIZER.route) { 
                VisualizerScreen() 
            }
            composable(Screen.EQUALIZER.route) { 
                EqualizerScreen() 
            }
            composable(Screen.SETTINGS.route) { 
                SettingsScreen() 
            }
        }
    }
}

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)
