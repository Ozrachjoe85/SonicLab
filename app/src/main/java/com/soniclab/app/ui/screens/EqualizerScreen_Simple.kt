package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.theme.*

@Composable
fun EqualizerScreen(
    modifier: Modifier = Modifier
) {
    val themeManager = LocalThemeManager.current
    val colors = themeManager.colors
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "EQUALIZER",
                color = colors.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Theme: ${themeManager.currentTheme.name}",
                color = colors.textSecondary,
                fontSize = 14.sp
            )
        }
    }
}
