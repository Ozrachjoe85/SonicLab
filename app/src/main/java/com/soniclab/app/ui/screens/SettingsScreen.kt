package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val colors = sonicColors
    val scrollState = rememberScrollState()
    
    var theme by remember { mutableStateOf("Void Lab (Dark)") }
    var audioQuality by remember { mutableStateOf("High") }
    var gaplessPlayback by remember { mutableStateOf(true) }
    var downloadOnWifiOnly by remember { mutableStateOf(true) }
    var showNotifications by remember { mutableStateOf(true) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(scrollState)
            .padding(24.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = "SETTINGS",
            color = colors.textPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Appearance
        SettingsSection("APPEARANCE") {
            SettingsDropdown(
                label = "Theme",
                value = theme,
                options = listOf("Void Lab (Dark)", "Void Lab (Light)", "Pure Black (AMOLED)"),
                onValueChange = { theme = it }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Playback
        SettingsSection("PLAYBACK") {
            SettingsDropdown(
                label = "Audio Quality",
                value = audioQuality,
                options = listOf("Low (128kbps)", "Medium (192kbps)", "High (320kbps)", "Lossless"),
                onValueChange = { audioQuality = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = colors.border.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingsSwitch(
                label = "Gapless Playback",
                description = "Seamless transitions between tracks",
                checked = gaplessPlayback,
                onCheckedChange = { gaplessPlayback = it }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Downloads
        SettingsSection("DOWNLOADS") {
            SettingsSwitch(
                label = "Download on Wi-Fi Only",
                description = "Save mobile data",
                checked = downloadOnWifiOnly,
                onCheckedChange = { downloadOnWifiOnly = it }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Notifications
        SettingsSection("NOTIFICATIONS") {
            SettingsSwitch(
                label = "Show Notifications",
                description = "Display playback controls",
                checked = showNotifications,
                onCheckedChange = { showNotifications = it }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Storage
        SettingsSection("STORAGE") {
            SettingsItem(
                label = "Clear Cache",
                description = "Free up space",
                onClick = { /* TODO */ }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = colors.border.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingsItem(
                label = "Storage Location",
                description = "/storage/emulated/0/SonicLab",
                onClick = { /* TODO */ }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // About
        SettingsSection("ABOUT") {
            SettingsItem(
                label = "Version",
                description = "1.0.0 (Phase 2)",
                onClick = {}
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = colors.border.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingsItem(
                label = "Licenses",
                description = "Open source licenses",
                onClick = { /* TODO */ }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = colors.border.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingsItem(
                label = "Privacy Policy",
                description = "How we handle your data",
                onClick = { /* TODO */ }
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
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
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsSwitch(
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
                checkedTrackColor = colors.primary.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun SettingsItem(
    label: String,
    description: String,
    onClick: () -> Unit
) {
    val colors = sonicColors
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
        
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = colors.textTertiary
        )
    }
}

@Composable
private fun SettingsDropdown(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    val colors = sonicColors
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Text(
            text = label,
            color = colors.textSecondary,
            fontSize = 13.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.textPrimary
                )
            ) {
                Text(value, modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
