package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.theme.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WalnutDark)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Title
        Text(
            text = "SONICLAB",
            style = MaterialTheme.typography.headlineLarge,
            color = EngravingGold,
            fontWeight = FontWeight.Bold,
            letterSpacing = 8.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "PROFESSIONAL AUDIO PLAYER",
            style = MaterialTheme.typography.labelMedium,
            color = LabelGray,
            letterSpacing = 4.sp
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Status Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(BrushedAluminum)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "READY",
                    style = MaterialTheme.typography.headlineMedium,
                    color = LEDGreen,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 6.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "No track loaded",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LabelGray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Instructions
        Text(
            text = "Navigate to Library to load music",
            style = MaterialTheme.typography.bodySmall,
            color = LabelWhite
        )
    }
}
