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
    var volumeValue by remember { mutableStateOf(0.7f) }
    var balanceValue by remember { mutableStateOf(0.5f) }
    var frequencyValue by remember { mutableStateOf(0.6f) }
    
    var quantumMode by remember { mutableStateOf(true) }
    var analogWarmth by remember { mutableStateOf(false) }
    var glitchFX by remember { mutableStateOf(true) }
    
    var isStreaming by remember { mutableStateOf(true) }
    
    val leftSignal by remember { mutableStateOf(0.68f) }
    val rightSignal by remember { mutableStateOf(0.75f) }
    
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
            // COSMIC HEADER
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
                            text = "◢ SONIC LAB ◣",
                            color = NeonCyan,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 4.sp
                        )
                        Text(
                            text = "CYBER-ANALOG-DIGITAL MATRIX",
                            color = PlasmaViolet,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        NeonLED(
                            isOn = true,
                            color = NeonGreen,
                            label = "SYS"
                        )
                        NeonLED(
                            isOn = isStreaming,
                            color = NeonPink,
                            label = "TX"
                        )
                    }
                }
            }
            
            // ============================================
            // HOLOGRAPHIC VU METERS
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "▶ SIGNAL ANALYSIS",
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
                        HolographicVUMeter(
                            level = leftSignal,
                            label = "L-CHANNEL"
                        )
                        HolographicVUMeter(
                            level = rightSignal,
                            label = "R-CHANNEL"
                        )
                    }
                }
            }
            
            // ============================================
            // HOLOGRAPHIC TIME DISPLAY
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "◆ DATA STREAM ◆",
                        color = GridCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    HolographicDisplay(
                        text = "03:42"
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = if (isStreaming) "⟨ COSMIC FREQUENCY ACTIVE ⟩" else "⟨ SYSTEM STANDBY ⟩",
                        color = if (isStreaming) NeonPink else Gray300,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
            
            // ============================================
            // DATA STREAM REELS
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DataStreamReel(
                            isActive = isStreaming,
                            size = 55.dp
                        )
                        DataStreamReel(
                            isActive = isStreaming,
                            size = 55.dp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = if (isStreaming) "◀◀ TRANSMITTING ▶▶" else "◀ PAUSE ▶",
                        color = if (isStreaming) NeonCyan else PlasmaViolet,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp
                    )
                }
            }
            
            // ============================================
            // PLASMA CONTROL MATRIX
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "⦿ PLASMA MATRIX ⦿",
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
                        PlasmaKnob(
                            value = volumeValue,
                            onValueChange = { volumeValue = it },
                            label = "AMPLITUDE"
                        )
                        PlasmaKnob(
                            value = balanceValue,
                            onValueChange = { balanceValue = it },
                            label = "BALANCE"
                        )
                        PlasmaKnob(
                            value = frequencyValue,
                            onValueChange = { frequencyValue = it },
                            label = "FREQUENCY"
                        )
                    }
                }
            }
            
            // ============================================
            // ENERGY SWITCHES
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "⚡ SYSTEM MATRIX ⚡",
                        color = EnergyFlare,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        EnergySwitch(
                            checked = quantumMode,
                            onCheckedChange = { quantumMode = it },
                            label = "QUANTUM"
                        )
                        EnergySwitch(
                            checked = analogWarmth,
                            onCheckedChange = { analogWarmth = it },
                            label = "ANALOG"
                        )
                        EnergySwitch(
                            checked = glitchFX,
                            onCheckedChange = { glitchFX = it },
                            label = "GLITCH"
                        )
                    }
                }
            }
        }
    }
}
