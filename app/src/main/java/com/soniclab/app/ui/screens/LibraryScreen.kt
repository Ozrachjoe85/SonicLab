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

@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.LibraryMusic,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Gray300
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Music Library",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Text(
            text = "Coming in Phase 2",
            style = MaterialTheme.typography.bodyMedium,
            color = Gray300
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Will scan your device for music files\nand organize them beautifully",
            style = MaterialTheme.typography.bodySmall,
            color = Gray300.copy(alpha = 0.7f)
        )
    }
}
