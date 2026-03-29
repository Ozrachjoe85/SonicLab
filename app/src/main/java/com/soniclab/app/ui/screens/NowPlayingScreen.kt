package com.soniclab.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.soniclab.app.playback.PlayerManager
import com.soniclab.app.playback.RepeatMode
import com.soniclab.app.audio.IntelligentEQManager
import com.soniclab.app.audio.EQMode
import com.soniclab.app.ui.theme.sonicColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val playerManager: PlayerManager,
    private val eqManager: IntelligentEQManager
) : ViewModel() {
    
    val currentTrack = playerManager.currentTrack
    val isPlaying = playerManager.isPlaying
    val currentPosition = playerManager.currentPosition
    val duration = playerManager.duration
    val shuffleEnabled = playerManager.shuffleEnabled
    val repeatMode = playerManager.repeatMode
    
    val eqMode = eqManager.mode
    val currentProfile = eqManager.currentProfile
    
    fun togglePlayPause() = playerManager.togglePlayPause()
    fun playNext() = playerManager.playNext()
    fun playPrevious() = playerManager.playPrevious()
    fun seekTo(position: Long) = playerManager.seekTo(position)
    fun toggleShuffle() = playerManager.toggleShuffle()
    fun cycleRepeat() = playerManager.cycleRepeatMode()
}

@Composable
fun NowPlayingScreen(
    modifier: Modifier = Modifier,
    viewModel: NowPlayingViewModel = hiltViewModel()
) {
    val colors = sonicColors
    val track by viewModel.currentTrack.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val position by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val shuffleEnabled by viewModel.shuffleEnabled.collectAsState()
    val repeatMode by viewModel.repeatMode.collectAsState()
    val eqMode by viewModel.eqMode.collectAsState()
    val profile by viewModel.currentProfile.collectAsState()
    
    // Update position every 100ms when playing
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(100)
            // Force recomposition
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Blurred background album art
        track?.albumArtUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(100.dp)
                    .scale(1.2f),
                contentScale = ContentScale.Crop,
                alpha = 0.15f
            )
        }
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            
            // Album art with glow animation
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Rotating glow
                val infiniteTransition = rememberInfiniteTransition(label = "glow")
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(10000, easing = LinearEasing),
                        repeatMode = RepeatMode.OFF
                    ),
                    label = "rotation"
                )
                
                if (isPlaying) {
                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .rotate(rotation)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        colors.primary.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )
                }
                
                // Album art
                Card(
                    modifier = Modifier.size(280.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    AsyncImage(
                        model = track?.albumArtUri,
                        contentDescription = "Album Art",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            Spacer(Modifier.height(32.dp))
            
            // Learning badge
            if (eqMode == EQMode.AUTO && profile != null) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically()
                ) {
                    LearningBadge(profile!!)
                }
                Spacer(Modifier.height(16.dp))
            }
            
            // Song info
            Text(
                text = track?.title ?: "No Track Playing",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = track?.artist ?: "",
                fontSize = 18.sp,
                color = colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(Modifier.height(32.dp))
            
            // Progress bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = position.toFloat(),
                    onValueChange = { viewModel.seekTo(it.toLong()) },
                    valueRange = 0f..duration.toFloat().coerceAtLeast(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = colors.primary,
                        activeTrackColor = colors.primary,
                        inactiveTrackColor = colors.border
                    )
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        formatTime(position),
                        fontSize = 12.sp,
                        color = colors.textTertiary
                    )
                    Text(
                        formatTime(duration),
                        fontSize = 12.sp,
                        color = colors.textTertiary
                    )
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle
                IconButton(onClick = { viewModel.toggleShuffle() }) {
                    Icon(
                        Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (shuffleEnabled) colors.primary else colors.textTertiary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                // Previous
                IconButton(onClick = { viewModel.playPrevious() }) {
                    Icon(
                        Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = colors.textPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                }
                
                // Play/Pause
                FloatingActionButton(
                    onClick = { viewModel.togglePlayPause() },
                    containerColor = colors.primary,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                // Next
                IconButton(onClick = { viewModel.playNext() }) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = colors.textPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                }
                
                // Repeat
                IconButton(onClick = { viewModel.cycleRepeat() }) {
                    Icon(
                        when (repeatMode) {
                            RepeatMode.ONE -> Icons.Default.RepeatOne
                            else -> Icons.Default.Repeat
                        },
                        contentDescription = "Repeat",
                        tint = if (repeatMode != RepeatMode.OFF) colors.primary else colors.textTertiary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LearningBadge(profile: com.soniclab.app.database.SongProfileEntity) {
    val colors = sonicColors
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colors.primary.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, colors.primary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(20.dp)
            )
            
            Column {
                Text(
                    if (profile.isLearned) "OPTIMIZED" else "LEARNING",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )
                Text(
                    "${(profile.confidence * 100).roundToInt()}% confidence • ${profile.playCount} plays",
                    fontSize = 10.sp,
                    color = colors.textSecondary
                )
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / 1000) / 60
    return String.format("%d:%02d", minutes, seconds)
}
