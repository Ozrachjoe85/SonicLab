package com.soniclab.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.components.*
import com.soniclab.app.ui.theme.*
import kotlin.math.*

enum class VisualizerStyle {
    FREQUENCY_BARS,
    WAVEFORM,
    CIRCULAR_PULSE,
    SPECTRUM_ANALYZER,
    VU_METERS,
    PARTICLE_FIELD,
    OSCILLOSCOPE,
    SPATIAL_3D
}

@Composable
fun VisualizerScreen(
    modifier: Modifier = Modifier
) {
    var currentStyle by remember { mutableStateOf(VisualizerStyle.FREQUENCY_BARS) }
    var isPlaying by remember { mutableStateOf(true) }
    
    // Simulated audio data (will be real audio in production)
    val audioData = remember {
        List(64) { (it % 20) * 0.05f + Random.nextFloat() * 0.3f }
    }
    
    CosmicVoidPanel(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ============================================
            // HEADER WITH STYLE SELECTOR
            // ============================================
            DigitalGridPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "◆ VISUAL MATRIX ◆",
                        color = NeonCyan,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    // Style selector buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StyleButton(
                            icon = Icons.Default.BarChart,
                            label = "BARS",
                            isSelected = currentStyle == VisualizerStyle.FREQUENCY_BARS,
                            onClick = { currentStyle = VisualizerStyle.FREQUENCY_BARS }
                        )
                        StyleButton(
                            icon = Icons.Default.GraphicEq,
                            label = "WAVE",
                            isSelected = currentStyle == VisualizerStyle.WAVEFORM,
                            onClick = { currentStyle = VisualizerStyle.WAVEFORM }
                        )
                        StyleButton(
                            icon = Icons.Default.Brightness1,
                            label = "PULSE",
                            isSelected = currentStyle == VisualizerStyle.CIRCULAR_PULSE,
                            onClick = { currentStyle = VisualizerStyle.CIRCULAR_PULSE }
                        )
                        StyleButton(
                            icon = Icons.Default.Analytics,
                            label = "SPECTRUM",
                            isSelected = currentStyle == VisualizerStyle.SPECTRUM_ANALYZER,
                            onClick = { currentStyle = VisualizerStyle.SPECTRUM_ANALYZER }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StyleButton(
                            icon = Icons.Default.Speed,
                            label = "VU",
                            isSelected = currentStyle == VisualizerStyle.VU_METERS,
                            onClick = { currentStyle = VisualizerStyle.VU_METERS }
                        )
                        StyleButton(
                            icon = Icons.Default.BubbleChart,
                            label = "PARTICLES",
                            isSelected = currentStyle == VisualizerStyle.PARTICLE_FIELD,
                            onClick = { currentStyle = VisualizerStyle.PARTICLE_FIELD }
                        )
                        StyleButton(
                            icon = Icons.Default.ShowChart,
                            label = "SCOPE",
                            isSelected = currentStyle == VisualizerStyle.OSCILLOSCOPE,
                            onClick = { currentStyle = VisualizerStyle.OSCILLOSCOPE }
                        )
                        StyleButton(
                            icon = Icons.Default.ThreeDRotation,
                            label = "3D",
                            isSelected = currentStyle == VisualizerStyle.SPATIAL_3D,
                            onClick = { currentStyle = VisualizerStyle.SPATIAL_3D }
                        )
                    }
                }
            }
            
            // ============================================
            // VISUALIZATION DISPLAY
            // ============================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                when (currentStyle) {
                    VisualizerStyle.FREQUENCY_BARS -> FrequencyBarsVisualization(audioData, isPlaying)
                    VisualizerStyle.WAVEFORM -> WaveformVisualization(audioData, isPlaying)
                    VisualizerStyle.CIRCULAR_PULSE -> CircularPulseVisualization(audioData, isPlaying)
                    VisualizerStyle.SPECTRUM_ANALYZER -> SpectrumAnalyzerVisualization(audioData, isPlaying)
                    VisualizerStyle.VU_METERS -> VUMetersVisualization(audioData, isPlaying)
                    VisualizerStyle.PARTICLE_FIELD -> ParticleFieldVisualization(audioData, isPlaying)
                    VisualizerStyle.OSCILLOSCOPE -> OscilloscopeVisualization(audioData, isPlaying)
                    VisualizerStyle.SPATIAL_3D -> Spatial3DVisualization(audioData, isPlaying)
                }
                
                // Style label overlay
                Text(
                    text = currentStyle.name.replace("_", " "),
                    color = NeonCyan.copy(alpha = 0.3f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            }
            
            // ============================================
            // INFO DISPLAY
            // ============================================
            DigitalGridPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "COSMIC FREQUENCY",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "192kHz • 24-bit • FLAC",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    
                    NeonLED(
                        isOn = isPlaying,
                        color = NeonPink,
                        label = "ACTIVE"
                    )
                }
            }
        }
    }
}

@Composable
private fun StyleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (isSelected) {
                        Brush.radialGradient(listOf(NeonCyan, PlasmaViolet))
                    } else {
                        Brush.radialGradient(listOf(Color(0xFF333333), Color(0xFF222222)))
                    },
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) TextPrimary else Gray300,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) NeonCyan else Gray300,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

// ============================================
// VISUALIZATION IMPLEMENTATIONS
// ============================================

@Composable
private fun FrequencyBarsVisualization(audioData: List<Float>, isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "Bars Animation")
    val animPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase"
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val barCount = audioData.size
        val barWidth = size.width / barCount
        
        audioData.forEachIndexed { index, level ->
            val animatedLevel = if (isPlaying) {
                level + sin((animPhase + index * 0.1f) * PI.toFloat() * 2) * 0.1f
            } else {
                level * 0.5f
            }
            
            val barHeight = animatedLevel.coerceIn(0f, 1f) * size.height
            val x = index * barWidth
            
            val color = when {
                index < barCount * 0.3f -> VUCosmicLow
                index < barCount * 0.7f -> VUCosmicMid
                else -> VUCosmicHigh
            }
            
            // Main bar
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(color, color.copy(alpha = 0.3f))
                ),
                topLeft = Offset(x + 2f, size.height - barHeight),
                size = Size(barWidth - 4f, barHeight)
            )
            
            // Glow on peak
            if (animatedLevel > 0.8f) {
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(VUCosmicPeak, Color.Transparent)
                    ),
                    radius = barWidth,
                    center = Offset(x + barWidth / 2, size.height - barHeight)
                )
            }
        }
    }
}

@Composable
private fun WaveformVisualization(audioData: List<Float>, isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "Wave Animation")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Wave Phase"
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = Path()
        val centerY = size.height / 2
        val amplitude = size.height * 0.3f
        
        path.moveTo(0f, centerY)
        
        for (i in 0..200) {
            val x = (i / 200f) * size.width
            val dataIndex = (i * audioData.size / 200).coerceIn(0, audioData.size - 1)
            val baseY = sin(i * 0.1f + phase) * amplitude * (if (isPlaying) 1f else 0.5f)
            val dataY = (audioData[dataIndex] - 0.5f) * amplitude
            val y = centerY + baseY + dataY
            
            path.lineTo(x, y)
        }
        
        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                listOf(NeonCyan, NeonPink, NeonPurple)
            ),
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun CircularPulseVisualization(audioData: List<Float>, isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val baseRadius = minOf(size.width, size.height) * 0.2f
        
        rotate(if (isPlaying) rotation else 0f) {
            audioData.forEachIndexed { index, level ->
                val angle = (index / audioData.size.toFloat()) * 360f
                val radius = baseRadius + level * 150f
                
                val color = when {
                    index < audioData.size * 0.3f -> NeonCyan
                    index < audioData.size * 0.7f -> NeonPink
                    else -> NeonPurple
                }
                
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(color.copy(alpha = 0.6f), Color.Transparent),
                        center = Offset(centerX, centerY),
                        radius = radius
                    ),
                    radius = radius,
                    center = Offset(centerX, centerY),
                    style = Stroke(width = 2f)
                )
            }
        }
    }
}

@Composable
private fun SpectrumAnalyzerVisualization(audioData: List<Float>, isPlaying: Boolean) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val segmentHeight = size.height / 30
        val barWidth = size.width / audioData.size
        
        audioData.forEachIndexed { index, level ->
            val segments = (level * 30 * (if (isPlaying) 1f else 0.5f)).toInt()
            val x = index * barWidth
            
            for (segment in 0 until segments) {
                val y = size.height - (segment + 1) * segmentHeight
                val color = when {
                    segment < 18 -> VUCosmicLow
                    segment < 25 -> VUCosmicMid
                    else -> VUCosmicHigh
                }
                
                drawRect(
                    color = color,
                    topLeft = Offset(x + 2f, y + 2f),
                    size = Size(barWidth - 4f, segmentHeight - 4f)
                )
            }
        }
    }
}

@Composable
private fun VUMetersVisualization(audioData: List<Float>, isPlaying: Boolean) {
    val leftLevel = audioData.take(audioData.size / 2).average().toFloat()
    val rightLevel = audioData.drop(audioData.size / 2).average().toFloat()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HolographicVUMeter(
            level = if (isPlaying) leftLevel else leftLevel * 0.3f,
            label = "LEFT CHANNEL"
        )
        Spacer(modifier = Modifier.height(32.dp))
        HolographicVUMeter(
            level = if (isPlaying) rightLevel else rightLevel * 0.3f,
            label = "RIGHT CHANNEL"
        )
    }
}

@Composable
private fun ParticleFieldVisualization(audioData: List<Float>, isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "Particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Time"
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        audioData.forEachIndexed { index, level ->
            val particleCount = (level * 20 * (if (isPlaying) 1f else 0.3f)).toInt()
            
            for (p in 0 until particleCount) {
                val angle = (index * 2 * PI / audioData.size + p * 0.3f + time * 0.01f).toFloat()
                val radius = 50f + level * 200f + p * 20f
                val x = size.width / 2 + cos(angle) * radius
                val y = size.height / 2 + sin(angle) * radius
                
                val color = when {
                    index < audioData.size * 0.3f -> NeonCyan
                    index < audioData.size * 0.7f -> NeonPink
                    else -> NeonPurple
                }
                
                drawCircle(
                    color = color.copy(alpha = 0.6f),
                    radius = 3f + level * 5f,
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Composable
private fun OscilloscopeVisualization(audioData: List<Float>, isPlaying: Boolean) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Grid
        val gridSpacing = 40f
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = GridCyan.copy(alpha = 0.1f),
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += gridSpacing
        }
        var y = 0f
        while (y < size.height) {
            drawLine(
                color = GridCyan.copy(alpha = 0.1f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += gridSpacing
        }
        
        // Trace
        val path = Path()
        val centerY = size.height / 2
        path.moveTo(0f, centerY)
        
        audioData.forEachIndexed { index, level ->
            val x = (index / audioData.size.toFloat()) * size.width
            val y = centerY + (level - 0.5f) * size.height * (if (isPlaying) 0.8f else 0.3f)
            path.lineTo(x, y)
        }
        
        drawPath(
            path = path,
            color = VUCosmicLow,
            style = Stroke(width = 2f)
        )
    }
}

@Composable
private fun Spatial3DVisualization(audioData: List<Float>, isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "3D Rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "3D Rotation"
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        
        rotate(if (isPlaying) rotation else 0f) {
            audioData.forEachIndexed { index, level ->
                val angle = (index / audioData.size.toFloat()) * 2 * PI
                val radius = 100f + level * 150f
                
                // Pseudo-3D effect
                val z = sin(angle.toFloat() + rotation * PI.toFloat() / 180f) * 50f
                val scale = 1f + z / 200f
                
                val x = centerX + cos(angle.toFloat()) * radius * scale
                val y = centerY + sin(angle.toFloat()) * radius * scale
                
                val color = when {
                    z > 0 -> NeonCyan
                    else -> NeonPurple
                }.copy(alpha = 0.5f + z / 100f)
                
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(color, Color.Transparent)
                    ),
                    radius = 10f * scale * (if (isPlaying) 1f else 0.5f),
                    center = Offset(x, y)
                )
            }
        }
    }
}
