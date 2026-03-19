package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.soniclab.app.ui.theme.Gray300

@Composable
fun EqualizerScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Gray300)
            .padding(16.dp)
    ) {
        Text(
            text = "Equalizer Screen",
            color = Gray300
        )
        
        // Add your equalizer sliders and controls here
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Bass",
            color = Gray300
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Treble",
            color = Gray300
        )
    }
}
