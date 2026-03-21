package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.theme.sonicColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerScreen(
    modifier: Modifier = Modifier
) {
    val colors = sonicColors
    val scrollState = rememberScrollState()
    
    var isEnabled by remember { mutableStateOf(false) }
    var selectedPreset by remember { mutableStateOf("Flat") }
    
    val presets = listOf("Flat", "Bass Boost", "Treble Boost", "Vocal Boost", "Rock", "Pop", "Classical", "Jazz")
    val bands = listOf("60Hz", "230Hz", "910Hz", "3.6kHz", "14kHz")
    val bandLevels = remember { mutableStateListOf(0f, 0f, 0f, 0f, 0f) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(scrollState)
            .padding(24.dp)
            .padding(bottom = 80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "EQUALIZER",
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            
            Switch(
                checked = isEnabled,
                onCheckedChange = { isEnabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colors.primary,
                    checkedTrackColor = colors.primary.copy(alpha = 0.5f)
                )
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "PRESETS",
            color = colors.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                presets.chunked(4).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { preset ->
                            FilterChip(
                                selected = selectedPreset == preset,
                                onClick = { 
                                    selectedPreset = preset
                                    when (preset) {
                                        "Bass Boost" -> {
                                            bandLevels[0] = 0.8f
                                            bandLevels[1] = 0.5f
                                            bandLevels[2] = 0f
                                            bandLevels[3] = 0f
                                            bandLevels[4] = 0f
                                        }
                                        "Treble Boost" -> {
                                            bandLevels[0] = 0f
                                            bandLevels[1] = 0f
                                            bandLevels[2] = 0f
                                            bandLevels[3] = 0.6f
                                            bandLevels[4] = 0.8f
                                        }
                                        else -> {
                                            for (i in bandLevels.indices) bandLevels[i] = 0f
                                        }
                                    }
                                },
                                label = { Text(preset, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colors.primary.copy(alpha = 0.2f),
                                    selectedLabelColor = colors.primary
                                )
                            )
                        }
                    }
                    if (row != presets.chunked(4).last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "FREQUENCY BANDS",
            color = colors.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "EQ UI ready - Full audio processing in Phase 3",
                    color = colors.textSecondary,
                    fontSize = 13.sp
                )
                
                bands.forEachIndexed { index, band ->
                    EQBandSlider(
                        frequency = band,
                        level = bandLevels.getOrElse(index) { 0f },
                        enabled = isEnabled,
                        onLevelChange = { bandLevels[index] = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun EQBandSlider(
    frequency: String,
    level: Float,
    enabled: Boolean,
    onLevelChange: (Float) -> Unit
) {
    val colors = sonicColors
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = frequency,
                color = if (enabled) colors.textPrimary else colors.textTertiary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = when {
                    level > 0 -> "+${(level * 10).toInt()}"
                    level < 0 -> "${(level * 10).toInt()}"
                    else -> "0"
                },
                color = if (enabled) colors.primary else colors.textTertiary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Slider(
            value = (level + 1f) / 2f,
            onValueChange = { onLevelChange(it * 2f - 1f) },
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = colors.primary,
                activeTrackColor = colors.primary,
                inactiveTrackColor = colors.border,
                disabledThumbColor = colors.textTertiary,
                disabledActiveTrackColor = colors.border
            )
        )
    }
}
