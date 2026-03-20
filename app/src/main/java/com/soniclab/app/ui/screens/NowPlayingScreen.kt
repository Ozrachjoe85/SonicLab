package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.adaptive.AdaptiveLayout
import com.soniclab.app.ui.adaptive.AdaptiveSizing
import com.soniclab.app.ui.adaptive.ScreenConfig
import com.soniclab.app.ui.theme.sonicColors
import com.soniclab.app.ui.theme.sonicEffects

@Composable
fun NowPlayingScreen(modifier: Modifier = Modifier) {
    AdaptiveLayout(
        compact = { config -> CompactNowPlaying(config, modifier) },
        medium = { config -> CompactNowPlaying(config, modifier) },
        wide = { config -> CompactNowPlaying(config, modifier) },
        extraWide = { config -> CompactNowPlaying(config, modifier) }
    )
}

@Composable
private fun CompactNowPlaying(config: ScreenConfig, modifier: Modifier = Modifier) {
    val colors = sonicColors
    val effects = sonicEffects
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0.35f) }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Album art
            Box(
                modifier = Modifier
                    .size(AdaptiveSizing.albumArtSize())
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                colors.primary.copy(alpha = 0.3f),
                                colors.secondary.copy(alpha = 0.2f),
                                colors.background
                            )
                        )
                    )
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Album,
                    contentDescription = "Album Art",
                    modifier = Modifier.size(120.dp),
                    tint = colors.textSecondary.copy(alpha = 0.3f)
                )
                
                if (effects.showHolographicRim) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        colors.primary.copy(alpha = 0.1f),
                                        colors.secondary.copy(alpha = 0.1f),
                                        colors.tertiary.copy(alpha = 0.1f)
                                    )
                                )
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "COSMIC FREQUENCY",
                color = colors.textPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "SONIC LAB • DIGITAL DREAMS",
                color = colors.textSecondary,
                fontSize = 13.sp,
                letterSpacing = 1.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Progress bar
            Column(modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = colors.primary,
                    trackColor = colors.border.copy(alpha = 0.3f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("1:23", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text("3:42", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // VU meters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("VU", color = colors.textTertiary, fontSize = 9.sp, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(8) { i ->
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(12.dp)
                                .background(
                                    when {
                                        i < 4 -> colors.vuLow.copy(alpha = 0.7f)
                                        i < 6 -> colors.vuMid.copy(alpha = 0.5f)
                                        else -> colors.vuHigh.copy(alpha = 0.3f)
                                    }
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("L", color = colors.textTertiary, fontSize = 9.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text("R", color = colors.textTertiary, fontSize = 9.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(8) { i ->
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(12.dp)
                                .background(
                                    when {
                                        i < 5 -> colors.vuLow.copy(alpha = 0.8f)
                                        i < 7 -> colors.vuMid.copy(alpha = 0.5f)
                                        else -> colors.vuHigh.copy(alpha = 0.2f)
                                    }
                                )
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Playback controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}, modifier = Modifier.size(AdaptiveSizing.touchTarget())) {
                    Icon(Icons.Default.SkipPrevious, "Previous", modifier = Modifier.size(32.dp), tint = colors.textPrimary)
                }
                
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    if (isPlaying) colors.primary else colors.secondary,
                                    if (isPlaying) colors.primary.copy(alpha = 0.3f) else colors.secondary.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                        .clickable { isPlaying = !isPlaying },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(40.dp),
                        tint = colors.textPrimary
                    )
                }
                
                IconButton(onClick = {}, modifier = Modifier.size(AdaptiveSizing.touchTarget())) {
                    Icon(Icons.Default.SkipNext, "Next", modifier = Modifier.size(32.dp), tint = colors.textPrimary)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Quick actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = {}) { Icon(Icons.Default.Shuffle, "Shuffle", tint = colors.textSecondary) }
                IconButton(onClick = {}) { Icon(Icons.Default.Repeat, "Repeat", tint = colors.textSecondary) }
                IconButton(onClick = {}) { Icon(Icons.Default.FavoriteBorder, "Favorite", tint = colors.textSecondary) }
                IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, "More", tint = colors.textSecondary) }
            }
        }
    }
}
