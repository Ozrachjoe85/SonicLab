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
fun EqualizerScreen(
    modifier: Modifier = Modifier
) {
    // EQ band values (32 bands - from 20Hz to 20kHz)
    var eqBands by remember {
        mutableStateOf(List(32) { 0.5f }) // 0.5 = center/flat
    }
    
    // Preset selector
    var selectedPreset by remember { mutableStateOf("FLAT") }
    
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
            // HEADER
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
                            text = "GRAPHIC EQUALIZER",
                            color = EngravingGold,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        Text(
                            text = "32-BAND PARAMETRIC",
                            color = LabelGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                    
                    LEDIndicator(
                        isOn = true,
                        color = LEDGreen,
                        label = "EQ"
                    )
                }
            }
            
            // ============================================
            // PRESET SELECTION
            // ============================================
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "PRESETS",
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
                        listOf("FLAT", "ROCK", "JAZZ", "VOCAL", "BASS").forEach { preset ->
                            Text(
                                text = preset,
                                color = if (selectedPreset == preset) NixieTubeOrange else LabelGray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
            
            // ============================================
            // FREQUENCY BANDS DISPLAY
            // ============================================
            BrushedMetalPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column {
                    Text(
                        text = "FREQUENCY RESPONSE",
                        color = LabelGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Simplified EQ band visualization
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        eqBands.forEachIndexed { index, level ->
                            // Vertical bar representing each frequency band
                            Box(
                                modifier = Modifier
                                    .width(8.dp)
                                    .fillMaxHeight(level)
                                    .padding(horizontal = 1.dp)
                            ) {
                                // Color based on frequency range
                                val color = when {
                                    index < 8 -> VURed      // Bass
                                    index < 24 -> VUGreen    // Mids
                                    else -> VUAmber          // Highs
                                }
                                
                                androidx.compose.foundation.Canvas(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    drawRect(color = color)
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Frequency labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "20 Hz",
                            color = LabelGray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "1 kHz",
                            color = LabelGray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "20 kHz",
                            color = LabelGray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // ============================================
            // MASTER CONTROLS
            // ============================================
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "MASTER CONTROLS",
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
                        var masterGain by remember { mutableStateOf(0.5f) }
                        var bassBoost by remember { mutableStateOf(0.3f) }
                        var trebleBoost by remember { mutableStateOf(0.4f) }
                        
                        RotaryKnob(
                            value = masterGain,
                            onValueChange = { masterGain = it },
                            label = "GAIN"
                        )
                        RotaryKnob(
                            value = bassBoost,
                            onValueChange = { bassBoost = it },
                            label = "BASS"
                        )
                        RotaryKnob(
                            value = trebleBoost,
                            onValueChange = { trebleBoost = it },
                            label = "TREBLE"
                        )
                    }
                }
            }
            
            // ============================================
            // STATUS DISPLAY
            // ============================================
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    NixieTubeDisplay(
                        text = "EQ ACTIVE"
                    )
                }
            }
        }
    }
}
