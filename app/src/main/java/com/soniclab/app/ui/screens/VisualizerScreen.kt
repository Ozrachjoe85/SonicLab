package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soniclab.app.ui.theme.Gray300
import com.soniclab.app.ui.visualizer.WaveformVisualizer

@Composable
fun VisualizerScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Visualizer",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Visualizer settings",
                    tint = Gray300
                )
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            WaveformVisualizer(
                modifier = Modifier.fillMaxSize(),
                isPlaying = true
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "More visualizer styles coming soon",
                style = MaterialTheme.typography.bodySmall,
                color = Gray300
            )
            Text(
                text = "Particles • Spectrum • Cinematic Scenes",
                style = MaterialTheme.typography.bodySmall,
                color = Gray300.copy(alpha = 0.7f)
            )
        }
    }
}
