package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.theme.sonicColors

/**
 * Placeholder screen for features not yet implemented
 */
@Composable
fun PlaceholderScreen(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    val colors = sonicColors
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Construction,
                contentDescription = "Under construction",
                modifier = Modifier.size(80.dp),
                tint = colors.primary.copy(alpha = 0.5f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = message,
                color = colors.textSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Coming in Phase 2",
                color = colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
