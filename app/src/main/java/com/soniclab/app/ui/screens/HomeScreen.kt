package com.soniclab.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.components.*
import com.soniclab.app.ui.theme.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    // State for interactive controls
    var volumeValue by remember { mutableStateOf(0.7f) }
    var balanceValue by remember { mutableStateOf(0.5f) }
    var toneValue by remember { mutableStateOf(0.6f) }
    
    var loudnessEnabled by remember { mutableStateOf(false) }
    var monoEnabled by remember { mutableStateOf(false) }
    var filterEnabled by remember { mutableStateOf(true) }
    
    var isPlaying by remember { mutableStateOf(true) }
    
    // Simulated VU meter levels (will be real audio levels in Phase 3)
    val leftLevel by remember { mutableStateOf(0.65f) }
    val rightLevel by remember { mutableStateOf(0.72f) }
    
    WoodPanel(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ============================================
            // HEADER - Brushed metal with LEDs
            // ============================================
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SONIC LAB",
                            color = EngravingGold,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 4.sp
                        )
                        Text(
                            text = "PROFESSIONAL AUDIO SYSTEM",
                            color = LabelGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
            
            // ============================================
            // VU METERS
            // ============================================
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "LEVEL METERS",
                        color = LabelGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        VUMeter(
                            level = leftLevel,
                            label = "LEFT"
                        )
                        VUMeter(
                            level = rightLevel,
                            label = "RIGHT"
                        )
                    }
                }
            }
            
            // ============================================
            // NOW PLAYING
            // ============================================
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NOW PLAYING",
                        color = LabelGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    NixieTubeDisplay(
                        text = "03:42"
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = if (isPlaying) "DEMO TRACK - SAMPLE AUDIO" else "READY - NO TRACK",
                        color = if (isPlaying) LabelWhite else LabelGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
            
            // ============================================
            // TAPE DECK
            // ============================================
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TapeReel(
                        isPlaying = isPlaying,
                        size = 50.dp
                    )
                    TapeReel(
                        isPlaying = isPlaying,
                        size = 50.dp
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = if (isPlaying) "⏸ PAUSE" else "▶ PLAY",
                    color = NixieTubeOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            
            // ============================================
            // ROTARY KNOBS
            // ============================================
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "AUDIO CONTROLS",
                        color = LabelGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RotaryKnob(
                            value = volumeValue,
                            onValueChange = { volumeValue = it },
                            label = "VOLUME"
                        )
                        RotaryKnob(
                            value = balanceValue,
                            onValueChange = { balanceValue = it },
                            label = "BALANCE"
                        )
                        RotaryKnob(
                            value = toneValue,
                            onValueChange = { toneValue = it },
                            label = "TONE"
                        )
                    }
                }
            }
            
            // ============================================
            // TOGGLE SWITCHES
            // ============================================
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ToggleSwitch(
                        checked = loudnessEnabled,
                        onCheckedChange = { loudnessEnabled = it },
                        label = "LOUDNESS"
                    )
                    ToggleSwitch(
                        checked = monoEnabled,
                        onCheckedChange = { monoEnabled = it },
                        label = "MONO"
                    )
                    ToggleSwitch(
                        checked = filterEnabled,
                        onCheckedChange = { filterEnabled = it },
                        label = "FILTER"
                    )
                }
            }
        }
    }
}
