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
fun EqualizerScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Equalizer,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Gray300
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "AutoEq Engine",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Text(
            text = "Coming in Phase 4",
            style = MaterialTheme.typography.bodyMedium,
            color = Gray300
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Automatic audio optimization\nReal-time frequency analysis\nUp to 32-band parametric EQ",
            style = MaterialTheme.typography.bodySmall,
            color = Gray300.copy(alpha = 0.7f)
        )
    }
}
