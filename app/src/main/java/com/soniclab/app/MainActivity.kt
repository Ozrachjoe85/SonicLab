package com.soniclab.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.soniclab.app.ui.screens.NowPlayingScreen
import com.soniclab.app.ui.theme.SonicLabTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity - Entry point for SonicLab
 * 
 * Currently shows only NowPlayingScreen
 * TODO: Add navigation and other screens
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SonicLabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // For now, just show Now Playing screen
                    // Navigation will be added in next phase
                    NowPlayingScreen()
                }
            }
        }
    }
}
