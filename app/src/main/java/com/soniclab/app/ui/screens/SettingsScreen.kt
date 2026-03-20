package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.components.*
import com.soniclab.app.ui.theme.*

enum class AppTheme {
    CYBER_DIGITAL_GHOST,
    PURE_ANALOG_WARMTH,
    TAPEDECK_RETRO,
    WALKMAN_PORTABLE,
    CAR_MODE,
    AUDIOPHILE_PRO,
    CUSTOM_MODULAR
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    val themeManager = LocalThemeManager.current
    val currentColors = themeManager.colors
    
    var showTapeReels by remember { mutableStateOf(true) }
    var showWaveform by remember { mutableStateOf(true) }
    var visualizationIntensity by remember { mutableStateOf(0.7f) }
    var carModeEnabled by remember { mutableStateOf(false) }
    var autoScanLibrary by remember { mutableStateOf(true) }
    var aiCategorization by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    
    CosmicVoidPanel(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                            text = "◢ SETTINGS ◣",
                            color = NeonCyan,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        Text(
                            text = "SYSTEM CONFIGURATION",
                            color = PlasmaBlue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                    
                    NeonLED(
                        isOn = true,
                        color = NeonGreen,
                        label = "SYS"
                    )
                }
            }
            
            // ============================================
            // THEME SELECTION
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "◆ THEME SELECTION ◆",
                        color = NeonPurple,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    // Theme grid
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemeOption(
                            theme = AppTheme.CYBER_DIGITAL_GHOST,
                            name = "Cyber-Digital-Ghost",
                            description = "Neon circuits, holographic displays, cosmic energy",
                            colors = listOf(NeonCyan, PlasmaViolet, NeonPink),
                            isSelected = themeManager.currentTheme == AppTheme.CYBER_DIGITAL_GHOST,
                            onClick = { themeManager.setTheme(AppTheme.CYBER_DIGITAL_GHOST) }
                        )
                        
                        ThemeOption(
                            theme = AppTheme.PURE_ANALOG_WARMTH,
                            name = "Pure Analog Warmth",
                            description = "Vintage tubes, warm glow, analog meters",
                            colors = listOf(Color(0xFFFF9955), Color(0xFFFFD700), Color(0xFFFF6E40)),
                            isSelected = themeManager.currentTheme == AppTheme.PURE_ANALOG_WARMTH,
                            onClick = { themeManager.setTheme(AppTheme.PURE_ANALOG_WARMTH) }
                        )
                        
                        ThemeOption(
                            theme = AppTheme.TAPEDECK_RETRO,
                            name = "Tapedeck Retro",
                            description = "Cassette aesthetic, spinning reels, mechanical",
                            colors = listOf(Color(0xFF8B4513), Color(0xFFC0C0C0), Color(0xFF000000)),
                            isSelected = themeManager.currentTheme == AppTheme.TAPEDECK_RETRO,
                            onClick = { themeManager.setTheme(AppTheme.TAPEDECK_RETRO) }
                        )
                        
                        ThemeOption(
                            theme = AppTheme.WALKMAN_PORTABLE,
                            name = "Walkman Portable",
                            description = "Compact Sony vibes, portable design, nostalgic",
                            colors = listOf(Color(0xFF4169E1), Color(0xFFFFD700), Color(0xFF000000)),
                            isSelected = themeManager.currentTheme == AppTheme.WALKMAN_PORTABLE,
                            onClick = { themeManager.setTheme(AppTheme.WALKMAN_PORTABLE) }
                        )
                        
                        ThemeOption(
                            theme = AppTheme.CAR_MODE,
                            name = "Car Mode",
                            description = "Large buttons, high contrast, driving optimized",
                            colors = listOf(Color(0xFFFF0000), Color(0xFFFFFFFF), Color(0xFF000000)),
                            isSelected = themeManager.currentTheme == AppTheme.CAR_MODE,
                            onClick = { themeManager.setTheme(AppTheme.CAR_MODE) }
                        )
                        
                        ThemeOption(
                            theme = AppTheme.AUDIOPHILE_PRO,
                            name = "Audiophile Pro",
                            description = "Clean minimal, waveform focused, professional",
                            colors = listOf(Color(0xFFFFFFFF), Color(0xFF333333), Color(0xFF00FFFF)),
                            isSelected = themeManager.currentTheme == AppTheme.AUDIOPHILE_PRO,
                            onClick = { themeManager.setTheme(AppTheme.AUDIOPHILE_PRO) }
                        )
                        
                        ThemeOption(
                            theme = AppTheme.CUSTOM_MODULAR,
                            name = "Custom Modular",
                            description = "Build your own - Mix & match components!",
                            colors = listOf(Color(0xFFFF00FF), Color(0xFF00FFFF), Color(0xFFFFFF00)),
                            isSelected = themeManager.currentTheme == AppTheme.CUSTOM_MODULAR,
                            onClick = { themeManager.setTheme(AppTheme.CUSTOM_MODULAR) }
                        )
                    }
                }
            }
            
            // ============================================
            // DISPLAY SETTINGS
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "▶ DISPLAY SETTINGS",
                        color = GridCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    SettingRow(
                        label = "Show Tape Reels",
                        description = "Display animated tape reels on Now Playing"
                    ) {
                        EnergySwitch(
                            checked = showTapeReels,
                            onCheckedChange = { showTapeReels = it },
                            label = ""
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    SettingRow(
                        label = "Show Waveform",
                        description = "Display mini waveform preview"
                    ) {
                        EnergySwitch(
                            checked = showWaveform,
                            onCheckedChange = { showWaveform = it },
                            label = ""
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    SettingRow(
                        label = "Car Mode",
                        description = "Larger buttons, higher contrast"
                    ) {
                        EnergySwitch(
                            checked = carModeEnabled,
                            onCheckedChange = { carModeEnabled = it },
                            label = ""
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Column {
                        Text(
                            text = "Visualization Intensity",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PlasmaKnob(
                            value = visualizationIntensity,
                            onValueChange = { visualizationIntensity = it },
                            label = "INTENSITY",
                            size = 60.dp
                        )
                    }
                }
            }
            
            // ============================================
            // AUDIO SETTINGS
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "⚡ AUDIO SETTINGS",
                        color = EnergyFlare,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    InfoRow(
                        label = "Audio Output",
                        value = "USB DAC (384kHz/32-bit)"
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    InfoRow(
                        label = "Sample Rate",
                        value = "192 kHz"
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    InfoRow(
                        label = "Bit Depth",
                        value = "24-bit"
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    InfoRow(
                        label = "Audio Engine",
                        value = "AutoEq Pro v3.2"
                    )
                }
            }
            
            // ============================================
            // LIBRARY SETTINGS
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "⦿ LIBRARY SETTINGS",
                        color = PlasmaMagenta,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    SettingRow(
                        label = "Auto-Scan Library",
                        description = "Automatically scan for new music"
                    ) {
                        EnergySwitch(
                            checked = autoScanLibrary,
                            onCheckedChange = { autoScanLibrary = it },
                            label = ""
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    SettingRow(
                        label = "AI Categorization",
                        description = "Smart mood & genre detection"
                    ) {
                        EnergySwitch(
                            checked = aiCategorization,
                            onCheckedChange = { aiCategorization = it },
                            label = ""
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(VUCosmicLow, VUCosmicMid)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { /* Scan library */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "◢ SCAN LIBRARY NOW ◣",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
            
            // ============================================
            // APP SETTINGS
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "◆ APP SETTINGS ◆",
                        color = GridPurple,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    SettingRow(
                        label = "Notifications",
                        description = "Show playback notifications"
                    ) {
                        EnergySwitch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            label = ""
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    InfoRow(
                        label = "Version",
                        value = "SonicLab v1.0.0"
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    InfoRow(
                        label = "Build",
                        value = "Cosmic-Punk-Alpha"
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "◢ ULTIMATE AUDIOPHILE APP ◣",
                        color = TextSecondary.copy(alpha = 0.5f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeOption(
    theme: AppTheme,
    name: String,
    description: String,
    colors: List<Color>,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                if (isSelected) {
                    Brush.horizontalGradient(
                        listOf(PlasmaViolet.copy(alpha = 0.3f), PlasmaCyan.copy(alpha = 0.3f))
                    )
                } else {
                    Brush.horizontalGradient(
                        listOf(Color(0xFF1A1A1A), Color(0xFF0D0D0D))
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                2.dp,
                if (isSelected) NeonCyan else Color(0xFF333333),
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    color = if (isSelected) NeonCyan else TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                )
            }
            
            // Color preview
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(color)
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingRow(
    label: String,
    description: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
        
        content()
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            color = NeonCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}
