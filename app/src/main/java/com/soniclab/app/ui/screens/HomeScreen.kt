package com.soniclab.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.components.*
import com.soniclab.app.ui.theme.*

@Composable
fun HomeScreen() {
    // Simulate audio levels and playback state
    var isPlaying by remember { mutableStateOf(false) }
    var volume by remember { mutableStateOf(0.7f) }
    var balance by remember { mutableStateOf(0.5f) }
    var tone by remember { mutableStateOf(0.5f) }
    
    // Animated VU meter levels
    val infiniteTransition = rememberInfiniteTransition(label = "vu levels")
    val leftLevel by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "left level"
    )
    val rightLevel by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "right level"
    )
    
    WoodPanel(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with app title
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SONIC LAB",
                            style = MaterialTheme.typography.headlineMedium,
                            color = EngravingGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 4.sp
                        )
                        Text(
                            text = "PROFESSIONAL AUDIO SYSTEM",
                            style = MaterialTheme.typography.labelSmall,
                            color = LabelGray,
                            letterSpacing = 2.sp
                        )
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LEDIndicator(
                            isOn = true,
                            color = LEDGreen,
                            label = "PWR"
                        )
                        LEDIndicator(
                            isOn = isPlaying,
                            color = LEDRed,
                            label = "REC"
                        )
                    }
                }
            }
            
            // VU Meters section
            Text(
                text = "LEVEL METERS",
                style = MaterialTheme.typography.labelLarge,
                color = MapleAccent,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VUMeter(
                    level = if (isPlaying) leftLevel else 0f,
                    label = "LEFT",
                    modifier = Modifier.weight(1f)
                )
                VUMeter(
                    level = if (isPlaying) rightLevel else 0f,
                    label = "RIGHT",
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Now Playing display
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "NOW PLAYING",
                        style = MaterialTheme.typography.labelMedium,
                        color = LabelGray,
                        letterSpacing = 2.sp
                    )
                    
                    NixieTubeDisplay(
                        text = if (isPlaying) "03:42" else "00:00"
                    )
                    
                    Text(
                        text = if (isPlaying) "DEMO TRACK - SAMPLE AUDIO" else "NO SOURCE SELECTED",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isPlaying) LabelWhite else LabelGray,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
            
            // Transport controls
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifi
