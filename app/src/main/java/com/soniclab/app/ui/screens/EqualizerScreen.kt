package com.soniclab.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.components.*
import com.soniclab.app.ui.theme.*
import kotlin.math.roundToInt

data class EQPreset(
    val name: String,
    val values: List<Float>
)

@Composable
fun EqualizerScreen(
    modifier: Modifier = Modifier
) {
    // EQ state
    var eqBands by remember {
        mutableStateOf(List(32) { 0.5f }) // 0.5 = center/flat (0dB)
    }
    
    var selectedPreset by remember { mutableStateOf("FLAT") }
    var masterGain by remember { mutableStateOf(0.5f) }
    
    // Simulated VU meter levels
    val leftLevel by remember { mutableStateOf(0.68f) }
    val rightLevel by remember { mutableStateOf(0.72f) }
    
    // EQ Presets
    val presets = remember {
        mapOf(
            "FLAT" to EQPreset("FLAT", List(32) { 0.5f }),
            "ROCK" to EQPreset("ROCK", List(32) { i ->
                when {
                    i < 4 -> 0.7f  // Bass boost
                    i < 8 -> 0.6f
                    i < 24 -> 0.5f // Mids neutral
                    else -> 0.65f  // Highs boost
                }
            }),
            "JAZZ" to EQPreset("JAZZ", List(32) { i ->
                when {
                    i < 6 -> 0.45f  // Bass cut
                    i < 16 -> 0.55f // Mids boost
                    else -> 0.6f    // Highs boost
                }
            }),
            "VOCAL" to EQPreset("VOCAL", List(32) { i ->
                when {
                    i < 8 -> 0.4f   // Bass cut
                    i < 20 -> 0.65f // Mids boost (vocals)
                    else -> 0.5f    // Highs neutral
                }
            }),
            "BASS" to EQPreset("BASS", List(32) { i ->
                when {
                    i < 10 -> 0.8f  // Heavy bass boost
                    i < 16 -> 0.55f
                    else -> 0.5f
                }
            })
        )
    }
    
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
                            text = "◢ EQUALIZER ◣",
                            color = NeonPurple,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        Text(
                            text = "32-BAND FREQUENCY CONTROL",
                            color = PlasmaBlue,
                            fontSize = 10.sp,
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
            // VU METERS (Top of EQ screen)
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "▶ AUDIO LEVELS",
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
                            level = leftLevel,
                            label = "L-CH",
                            modifier = Modifier.size(100.dp)
                        )
                        HolographicVUMeter(
                            level = rightLevel,
                            label = "R-CH",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            }
            
            // ============================================
            // PRESET SELECTION
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "◆ PRESETS ◆",
                        color = GridCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        presets.keys.forEach { presetName ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 2.dp)
                                    .height(36.dp)
                                    .background(
                                        if (selectedPreset == presetName) {
                                            Brush.horizontalGradient(
                                                listOf(PlasmaViolet, PlasmaCyan)
                                            )
                                        } else {
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFF222222), Color(0xFF111111))
                                            )
                                        },
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        selectedPreset = presetName
                                        eqBands = presets[presetName]?.values ?: eqBands
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = presetName,
                                    color = if (selectedPreset == presetName) TextPrimary else Gray300,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }
            }
            
            // ============================================
            // 32-BAND EQ SLIDERS
            // ============================================
            DigitalGridPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column {
                    Text(
                        text = "⦿ FREQUENCY BANDS ⦿",
                        color = PlasmaMagenta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // EQ Sliders visualization
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        eqBands.forEachIndexed { index, level ->
                            EQSlider(
                                value = level,
                                onValueChange = { newValue ->
                                    eqBands = eqBands.toMutableList().apply {
                                        this[index] = newValue
                                    }
                                    selectedPreset = "CUSTOM"
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(horizontal = 1.dp),
                                color = when {
                                    index < 8 -> VUCosmicLow
                                    index < 24 -> VUCosmicMid
                                    else -> VUCosmicHigh
                                }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Frequency labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "20Hz",
                            color = VUCosmicLow,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "200Hz",
                            color = VUCosmicLow,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "2kHz",
                            color = VUCosmicMid,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "20kHz",
                            color = VUCosmicHigh,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // dB scale
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "+12 dB",
                            color = TextSecondary,
                            fontSize = 9.sp
                        )
                        Text(
                            text = "0 dB",
                            color = TextSecondary,
                            fontSize = 9.sp
                        )
                        Text(
                            text = "-12 dB",
                            color = TextSecondary,
                            fontSize = 9.sp
                        )
                    }
                }
            }
            
            // ============================================
            // MASTER CONTROLS
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "⚡ MASTER CONTROLS ⚡",
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
                        PlasmaKnob(
                            value = masterGain,
                            onValueChange = { masterGain = it },
                            label = "MASTER",
                            size = 60.dp
                        )
                        
                        // Reset button
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(60.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(GlitchRed, PlasmaMagenta)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedPreset = "FLAT"
                                    eqBands = List(32) { 0.5f }
                                    masterGain = 0.5f
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "RESET\nFLAT",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                        
                        // Save button
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(60.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(VUCosmicLow, VUCosmicMid)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    // Save custom preset
                                    selectedPreset = "CUSTOM"
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SAVE\nCUSTOM",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EQSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = NeonCyan
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    val delta = -dragAmount / 200f
                    val newValue = (value + delta).coerceIn(0f, 1f)
                    onValueChange(newValue)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerY = size.height / 2
            val sliderHeight = value * size.height
            val sliderY = size.height - sliderHeight
            
            // Track (full height, dimmed)
            drawRect(
                color = color.copy(alpha = 0.2f),
                topLeft = Offset(0f, 0f),
                size = Size(size.width, size.height)
            )
            
            // Center line (0dB)
            drawRect(
                color = GridCyan.copy(alpha = 0.4f),
                topLeft = Offset(0f, centerY - 1f),
                size = Size(size.width, 2f)
            )
            
            // Active slider
            drawRect(
                brush = Brush.verticalGradient(
                    if (value > 0.5f) {
                        // Boost (above center)
                        listOf(color, color.copy(alpha = 0.5f))
                    } else {
                        // Cut (below center)
                        listOf(color.copy(alpha = 0.5f), color)
                    }
                ),
                topLeft = Offset(0f, minOf(sliderY, centerY)),
                size = Size(
                    size.width,
                    kotlin.math.abs(sliderY - centerY)
                )
            )
            
            // Glow on top
            if (value > 0.7f) {
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(VUCosmicPeak, Color.Transparent)
                    ),
                    radius = size.width * 2,
                    center = Offset(size.width / 2, sliderY)
                )
            }
        }
        
        // Value indicator
        val dbValue = ((value - 0.5f) * 24).roundToInt()
        if (dbValue != 0) {
            Text(
                text = "${if (dbValue > 0) "+" else ""}$dbValue",
                color = color,
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (value * 200).dp)
            )
        }
    }
}
