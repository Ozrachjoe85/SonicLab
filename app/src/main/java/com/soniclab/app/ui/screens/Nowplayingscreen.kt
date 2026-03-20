package com.soniclab.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.components.*
import com.soniclab.app.ui.theme.*

@Composable
fun NowPlayingScreen(
    modifier: Modifier = Modifier
) {
    // Playback state
    var isPlaying by remember { mutableStateOf(true) }
    var isShuffle by remember { mutableStateOf(false) }
    var repeatMode by remember { mutableStateOf(0) } // 0=off, 1=all, 2=one
    var isFavorite by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0.35f) }
    
    // Swipe panel state
    var isPanelOpen by remember { mutableStateOf(false) }
    val panelOffset by animateFloatAsState(
        targetValue = if (isPanelOpen) 0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Panel Slide"
    )
    
    // Audio controls (for panel)
    var volume by remember { mutableStateOf(0.7f) }
    var balance by remember { mutableStateOf(0.5f) }
    var tone by remember { mutableStateOf(0.6f) }
    var crossfadeEnabled by remember { mutableStateOf(true) }
    var gaplessEnabled by remember { mutableStateOf(true) }
    
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Main Now Playing Content
        CosmicVoidPanel(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                
                // ============================================
                // ALBUM ART (Large, Center)
                // ============================================
                Box(
                    modifier = Modifier
                        .size(320.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    PlasmaViolet,
                                    PlasmaMagenta,
                                    PlasmaBlue
                                )
                            )
                        )
                        .clickable { /* Future: Expand to full screen */ },
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder - will be album art
                    Icon(
                        imageVector = Icons.Default.Album,
                        contentDescription = "Album Art",
                        modifier = Modifier.size(120.dp),
                        tint = NeonCyan.copy(alpha = 0.5f)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // ============================================
                // TRACK INFO
                // ============================================
                Text(
                    text = "COSMIC FREQUENCY",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "SONIC LAB · DIGITAL DREAMS",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // ============================================
                // PROGRESS BAR
                // ============================================
                Column(modifier = Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = NeonCyan,
                        trackColor = GridCyan.copy(alpha = 0.2f)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "1:28",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "3:42",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // ============================================
                // PLAYBACK CONTROLS
                // ============================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous
                    IconButton(
                        onClick = { /* Previous track */ },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            modifier = Modifier.size(40.dp),
                            tint = TextPrimary
                        )
                    }
                    
                    // Play/Pause (Large, glowing)
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    listOf(
                                        if (isPlaying) NeonCyan else PlasmaViolet,
                                        if (isPlaying) NeonCyan.copy(alpha = 0.3f) else PlasmaViolet.copy(alpha = 0.3f),
                                        androidx.compose.ui.graphics.Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                            .clickable { isPlaying = !isPlaying },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            modifier = Modifier.size(48.dp),
                            tint = TextPrimary
                        )
                    }
                    
                    // Next
                    IconButton(
                        onClick = { /* Next track */ },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next",
                            modifier = Modifier.size(40.dp),
                            tint = TextPrimary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // ============================================
                // QUICK ACTIONS
                // ============================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Shuffle
                    IconButton(onClick = { isShuffle = !isShuffle }) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Shuffle",
                            tint = if (isShuffle) NeonCyan else Gray300
                        )
                    }
                    
                    // Repeat
                    IconButton(onClick = { repeatMode = (repeatMode + 1) % 3 }) {
                        Icon(
                            imageVector = when (repeatMode) {
                                1 -> Icons.Default.Repeat
                                2 -> Icons.Default.RepeatOne
                                else -> Icons.Default.Repeat
                            },
                            contentDescription = "Repeat",
                            tint = if (repeatMode > 0) NeonCyan else Gray300
                        )
                    }
                    
                    // Favorite
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) NeonPink else Gray300
                        )
                    }
                    
                    // More (opens swipe panel)
                    IconButton(onClick = { isPanelOpen = !isPanelOpen }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Audio Controls",
                            tint = if (isPanelOpen) NeonCyan else TextPrimary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Small hint to swipe up
                if (!isPanelOpen) {
                    Text(
                        text = "⬆ Swipe up for audio controls",
                        color = TextSecondary.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
        
        // ============================================
        // SWIPE-UP AUDIO CONTROLS PANEL
        // ============================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .align(Alignment.BottomCenter)
                .offset(y = (panelOffset * 1000).dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount < -50 && !isPanelOpen) {
                            isPanelOpen = true
                        } else if (dragAmount > 50 && isPanelOpen) {
                            isPanelOpen = false
                        }
                    }
                }
        ) {
            DigitalGridPanel(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Panel handle
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(GridCyan, RoundedCornerShape(2.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "⚡ AUDIO CONTROLS ⚡",
                        color = NeonCyan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Plasma knobs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        PlasmaKnob(
                            value = volume,
                            onValueChange = { volume = it },
                            label = "VOLUME",
                            size = 65.dp
                        )
                        PlasmaKnob(
                            value = balance,
                            onValueChange = { balance = it },
                            label = "BALANCE",
                            size = 65.dp
                        )
                        PlasmaKnob(
                            value = tone,
                            onValueChange = { tone = it },
                            label = "TONE",
                            size = 65.dp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Toggles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        EnergySwitch(
                            checked = crossfadeEnabled,
                            onCheckedChange = { crossfadeEnabled = it },
                            label = "CROSSFADE"
                        )
                        EnergySwitch(
                            checked = gaplessEnabled,
                            onCheckedChange = { gaplessEnabled = it },
                            label = "GAPLESS"
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Close button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(PlasmaViolet, PlasmaCyan)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { isPanelOpen = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "◢ CLOSE ◣",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }
    }
}
