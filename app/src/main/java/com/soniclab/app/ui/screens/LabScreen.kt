package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
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
import com.soniclab.app.ui.theme.sonicColors

/**
 * LabScreen - Audio controls and settings
 */
@Composable
fun LabScreen(modifier: Modifier = Modifier) {
    val colors = sonicColors
    val scrollState = rememberScrollState()
    
    var bassBoost by remember { mutableStateOf(0f) }
    var trebleBoost by remember { mutableStateOf(0f) }
    var volume by remember { mutableStateOf(0.7f) }
    var gaplessPlayback by remember { mutableStateOf(true) }
    var crossfade by remember { mutableStateOf(false) }
    var crossfadeDuration by remember { mutableStateOf(3f) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(scrollState)
            .padding(24.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = "AUDIO LAB",
            color = colors.textPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // EQ Section
        SectionCard(title = "EQUALIZER") {
            Column(modifier = Modifier.padding(16.dp)) {
                EQSlider(
                    label = "Bass",
                    value = bassBoost,
                    onValueChange = { bassBoost = it },
                    icon = Icons.Default.GraphicEq
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                EQSlider(
                    label = "Treble",
                    value = trebleBoost,
                    onValueChange = { trebleBoost = it },
                    icon = Icons.Default.Tune
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Full EQ coming in Phase 3",
                    color = colors.textTertiary,
                    fontSize = 12.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Volume Section
        SectionCard(title = "VOLUME") {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.VolumeDown,
                        contentDescription = null,
                        tint = colors.textSecondary
                    )
                    
                    Slider(
                        value = volume,
                        onValueChange = { volume = it },
                        modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = colors.primary,
                            activeTrackColor = colors.primary,
                            inactiveTrackColor = colors.border
                        )
                    )
                    
                    Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = colors.textSecondary
                    )
                }
                
                Text(
                    text = "${(volume * 100).toInt()}%",
                    color = colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Playback Settings
        SectionCard(title = "PLAYBACK") {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingSwitch(
                    label = "Gapless Playback",
                    description = "Seamless track transitions",
                    checked = gaplessPlayback,
                    onCheckedChange = { gaplessPlayback = it }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = colors.border.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))
                
                SettingSwitch(
                    label = "Crossfade",
                    description = "Fade between tracks",
                    checked = crossfade,
                    onCheckedChange = { crossfade = it }
                )
                
                if (crossfade) {
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Duration",
                            color = colors.textSecondary,
                            fontSize = 14.sp
                        )
                        
                        Slider(
                            value = crossfadeDuration,
                            onValueChange = { crossfadeDuration = it },
                            valueRange = 1f..10f,
                            steps = 8,
                            modifier = Modifier.width(150.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = colors.secondary,
                                activeTrackColor = colors.secondary,
                                inactiveTrackColor = colors.border
                            )
                        )
                        
                        Text(
                            text = "${crossfadeDuration.toInt()}s",
                            color = colors.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Phase 3: Full implementation",
                    color = colors.textTertiary,
                    fontSize = 12.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Audio Info
        SectionCard(title = "AUDIO INFO") {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow("Sample Rate", "44.1 kHz")
                InfoRow("Bit Depth", "16-bit")
                InfoRow("Codec", "AAC / MP3")
                InfoRow("Buffer Size", "Auto")
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Real-time audio analysis in Phase 3",
                    color = colors.textTertiary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    val colors = sonicColors
    
    Column {
        Text(
            text = title,
            color = colors.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            )
        ) {
            content()
        }
    }
}

@Composable
private fun EQSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    val colors = sonicColors
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colors.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Text(
                text = if (value > 0) "+${(value * 10).toInt()}" else "${(value * 10).toInt()}",
                color = colors.textSecondary,
                fontSize = 14.sp
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = -1f..1f,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = colors.primary,
                activeTrackColor = colors.primary,
                inactiveTrackColor = colors.border
            )
        )
    }
}

@Composable
private fun SettingSwitch(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = sonicColors
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = colors.textSecondary,
                fontSize = 13.sp
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colors.primary,
                checkedTrackColor = colors.primary.copy(alpha = 0.5f),
                uncheckedThumbColor = colors.textTertiary,
                uncheckedTrackColor = colors.border
            )
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    val colors = sonicColors
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = colors.textSecondary,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
