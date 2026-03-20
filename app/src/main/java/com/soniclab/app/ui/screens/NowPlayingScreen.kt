package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.soniclab.app.ui.adaptive.AdaptiveLayout
import com.soniclab.app.ui.adaptive.ScreenConfig
import com.soniclab.app.ui.theme.sonicColors
import com.soniclab.app.ui.theme.sonicEffects

@Composable
fun NowPlayingScreen(
    modifier: Modifier = Modifier,
    viewModel: NowPlayingViewModel = hiltViewModel()
) {
    AdaptiveLayout(
        compact = { config -> CompactNowPlaying(config, viewModel, modifier) },
        medium = { config -> CompactNowPlaying(config, viewModel, modifier) },
        wide = { config -> CompactNowPlaying(config, viewModel, modifier) },
        extraWide = { config -> CompactNowPlaying(config, viewModel, modifier) }
    )
}

@Composable
private fun CompactNowPlaying(
    config: ScreenConfig,
    viewModel: NowPlayingViewModel,
    modifier: Modifier = Modifier
) {
    val colors = sonicColors
    val effects = sonicEffects
    val scrollState = rememberScrollState()
    
    // Collect state from ViewModel
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentTrack by viewModel.currentTrack.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp)
                .padding(bottom = 80.dp), // Extra padding for bottom nav
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Album art
            Box(
                modifier = Modifier
                    .size(280.dp)
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
                    .clickable { viewModel.togglePlayPause() },
                contentAlignment = Alignment.Center
            ) {
                if (currentTrack?.albumArtUri != null) {
                    // Load actual album art
                    AsyncImage(
                        model = currentTrack?.albumArtUri,
                        contentDescription = "Album Art",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder
                    Icon(
                        imageVector = Icons.Default.Album,
                        contentDescription = "Album Art",
                        modifier = Modifier.size(120.dp),
                        tint = colors.textSecondary.copy(alpha = 0.3f)
                    )
                }
                
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
            
            // Track info
            Text(
                text = currentTrack?.title?.uppercase() ?: "NO TRACK LOADED",
                color = colors.textPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${currentTrack?.artist?.uppercase() ?: "UNKNOWN ARTIST"} • ${currentTrack?.album?.uppercase() ?: "UNKNOWN ALBUM"}",
                color = colors.textSecondary,
                fontSize = 13.sp,
                letterSpacing = 1.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Progress bar (now interactive!)
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = progress,
                    onValueChange = { viewModel.seekTo(it) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = colors.primary,
                        activeTrackColor = colors.primary,
                        inactiveTrackColor = colors.border.copy(alpha = 0.3f)
                    )
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(currentPosition, color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text(duration, color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // VU meters (react to playback state)
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
                                        i < 4 -> colors.vuLow.copy(alpha = if (isPlaying) 0.7f else 0.3f)
                                        i < 6 -> colors.vuMid.copy(alpha = if (isPlaying) 0.5f else 0.2f)
                                        else -> colors.vuHigh.copy(alpha = if (isPlaying) 0.3f else 0.1f)
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
                                        i < 5 -> colors.vuLow.copy(alpha = if (isPlaying) 0.8f else 0.3f)
                                        i < 7 -> colors.vuMid.copy(alpha = if (isPlaying) 0.5f else 0.2f)
                                        else -> colors.vuHigh.copy(alpha = if (isPlaying) 0.2f else 0.1f)
                                    }
                                )
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Playback controls (now functional!)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.playPrevious() },
                    modifier = Modifier.size(48.dp)
                ) {
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
                        .clickable { viewModel.togglePlayPause() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(40.dp),
                        tint = colors.textPrimary
                    )
                }
                
                IconButton(
                    onClick = { viewModel.playNext() },
                    modifier = Modifier.size(48.dp)
                ) {
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
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.primary
            )
        }
        
        // Error message
        errorMessage?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("OK", color = colors.primary)
                    }
                }
            ) {
                Text(error)
            }
        }
    }
}
