package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.theme.sonicColors

/**
 * Visualizer types available
 */
data class VisualizerType(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val comingSoon: Boolean = false
)

/**
 * VisualsScreen - Audio visualizations
 */
@Composable
fun VisualsScreen(modifier: Modifier = Modifier) {
    val colors = sonicColors
    var selectedVisualizer by remember { mutableStateOf("spectrum") }
    var isPlaying by remember { mutableStateOf(false) }
    
    val visualizers = listOf(
        VisualizerType(
            "spectrum",
            "Spectrum Cascade",
            "Waterfall frequency display",
            Icons.Default.BarChart
        ),
        VisualizerType(
            "radial",
            "Radial Pulse",
            "Circular burst visualization",
            Icons.Default.RadioButtonChecked
        ),
        VisualizerType(
            "particle",
            "Particle Storm",
            "10K audio-reactive particles",
            Icons.Default.BubbleChart,
            comingSoon = true
        ),
        VisualizerType(
            "lissajous",
            "Lissajous",
            "Stereo phase curves",
            Icons.Default.Timeline,
            comingSoon = true
        ),
        VisualizerType(
            "vu",
            "VU Classic",
            "Giant analog needles",
            Icons.Default.SpeedOutlined
        ),
        VisualizerType(
            "waveform",
            "Waveform Trace",
            "Oscilloscope style",
            Icons.Default.ShowChart
        ),
        VisualizerType(
            "grid",
            "Frequency Grid",
            "3D bar visualization",
            Icons.Default.GridOn,
            comingSoon = true
        ),
        VisualizerType(
            "void",
            "Void Field",
            "Minimal glowing dots",
            Icons.Default.FiberManualRecord
        )
    )
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(24.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = "VISUALIZERS",
            color = colors.textPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Preview area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            colors.primary.copy(alpha = 0.1f),
                            colors.secondary.copy(alpha = 0.1f)
                        )
                    )
                )
                .border(
                    1.dp,
                    colors.primary.copy(alpha = 0.3f),
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    visualizers.find { it.id == selectedVisualizer }?.icon ?: Icons.Default.GraphicEq,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = colors.primary.copy(alpha = 0.5f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = if (isPlaying) "PLAYING" else "TAP TO START",
                    color = colors.textSecondary,
                    fontSize = 12.sp,
                    letterSpacing = 2.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Full visualization in Phase 4",
                    color = colors.textTertiary,
                    fontSize = 11.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Visualizer grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(visualizers) { visualizer ->
                VisualizerCard(
                    visualizer = visualizer,
                    isSelected = selectedVisualizer == visualizer.id,
                    onClick = { selectedVisualizer = visualizer.id }
                )
            }
        }
    }
}

@Composable
private fun VisualizerCard(
    visualizer: VisualizerType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = sonicColors
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable(onClick = onClick, enabled = !visualizer.comingSoon),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) colors.primary.copy(alpha = 0.1f) else colors.surface
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(
                    listOf(colors.primary, colors.secondary)
                )
            )
        } else null
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        visualizer.icon,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = if (isSelected) colors.primary else colors.textSecondary
                    )
                    
                    if (visualizer.comingSoon) {
                        Text(
                            text = "SOON",
                            color = colors.secondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
                
                Column {
                    Text(
                        text = visualizer.name,
                        color = if (isSelected) colors.primary else colors.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = visualizer.description,
                        color = colors.textSecondary,
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }
            }
        }
    }
}
