package com.soniclab.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.components.*
import com.soniclab.app.ui.theme.*

@Composable
fun EqualizerScreen(
    modifier: Modifier = Modifier
) {
    var eqBands by remember {
        mutableStateOf(List(32) { 0.3f + (it % 7) * 0.1f })
    }
    
    var selectedPreset by remember { mutableStateOf("COSMIC") }
    
    CosmicVoidPanel(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ============================================
            // HEADER
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "◢ FREQUENCY MATRIX ◣",
                            color = NeonPurple,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        Text(
                            text = "32-BAND QUANTUM ANALYZER",
                            color = PlasmaBlue,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                    
                    NeonLED(
                        isOn = true,
                        color = NeonPink,
                        label = "EQ"
                    )
                }
            }
            
            // ============================================
            // PRESET MATRIX
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "▶ QUANTUM PRESETS",
                        color = GridPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("COSMIC", "NEBULA", "VOID", "PLASMA", "QUANTUM").forEach { preset ->
                            Text(
                                text = preset,
                                color = if (selectedPreset == preset) NeonCyan else Gray300,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
            
            // ============================================
            // FREQUENCY SPECTRUM VISUALIZATION
            // ============================================
            DigitalGridPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column {
                    Text(
                        text = "◆ SPECTRAL ANALYSIS ◆",
                        color = GridCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Holographic frequency bars
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val barWidth = size.width / eqBands.size
                            
                            eqBands.forEachIndexed { index, level ->
                                val x = index * barWidth
                                val barHeight = level * size.height
                                
                                // Determine color based on frequency range
                                val color = when {
                                    index < 8 -> VUCosmicLow    // Bass
                                    index < 24 -> VUCosmicMid   // Mids
                                    else -> VUCosmicHigh        // Highs
                                }
                                
                                // Draw bar with gradient
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            color,
                                            color.copy(alpha = 0.5f),
                                            color.copy(alpha = 0.2f)
                                        )
                                    ),
                                    topLeft = androidx.compose.ui.geometry.Offset(
                                        x + 1f,
                                        size.height - barHeight
                                    ),
                                    size = androidx.compose.ui.geometry.Size(
                                        barWidth - 2f,
                                        barHeight
                                    )
                                )
                                
                                // Glow effect on top
                                if (level > 0.7f) {
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            listOf(
                                                VUCosmicPeak,
                                                VUCosmicPeak.copy(alpha = 0.3f),
                                                androidx.compose.ui.graphics.Color.Transparent
                                            )
                                        ),
                                        radius = barWidth,
                                        center = androidx.compose.ui.geometry.Offset(
                                            x + barWidth / 2,
                                            size.height - barHeight
                                        )
                                    )
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
                            text = "◀ 20 Hz",
                            color = VUCosmicLow,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "1 kHz ◆",
                            color = VUCosmicMid,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "20 kHz ▶",
                            color = VUCosmicHigh,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // ============================================
            // PLASMA CONTROLS
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "⦿ PLASMA MODULATION ⦿",
                        color = PlasmaMagenta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        var masterGain by remember { mutableStateOf(0.5f) }
                        var lowFreq by remember { mutableStateOf(0.3f) }
                        var highFreq by remember { mutableStateOf(0.6f) }
                        
                        PlasmaKnob(
                            value = masterGain,
                            onValueChange = { masterGain = it },
                            label = "MASTER"
                        )
                        PlasmaKnob(
                            value = lowFreq,
                            onValueChange = { lowFreq = it },
                            label = "LOW-Ω"
                        )
                        PlasmaKnob(
                            value = highFreq,
                            onValueChange = { highFreq = it },
                            label = "HIGH-Ω"
                        )
                    }
                }
            }
            
            // ============================================
            // STATUS
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    HolographicDisplay(
                        text = "◢ ACTIVE ◣"
                    )
                }
            }
        }
    }
}
