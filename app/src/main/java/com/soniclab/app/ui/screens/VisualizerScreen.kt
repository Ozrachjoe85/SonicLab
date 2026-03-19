package com.soniclab.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.components.*
import com.soniclab.app.ui.theme.*
import kotlin.math.sin
import kotlin.math.PI

@Composable
fun VisualizerScreen() {
    var visualizerMode by remember { mutableStateOf(VisualizerMode.OSCILLOSCOPE) }
    
    WoodPanel(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "WAVEFORM ANALYZER",
                            style = MaterialTheme.typography.headlineSmall,
                            color = EngravingGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        Text(
                            text = visualizerMode.label.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = CRTGreen,
                            letterSpacing = 2.sp
                        )
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LEDIndicator(
                            isOn = true,
                            color = CRTGreen,
                            label = "SYNC"
                        )
                        LEDIndicator(
                            isOn = true,
                            color = LEDBlue,
                            label = "TRIG"
                        )
                    }
                }
            }
            
            // Main oscilloscope display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (visualizerMode) {
                    VisualizerMode.OSCILLOSCOPE -> OscilloscopeDisplay()
                    VisualizerMode.SPECTRUM -> SpectrumAnalyzerDisplay()
                    VisualizerMode.STEREO -> StereoPhaseDisplay()
                }
            }
            
            // Mode selector
            BrushedMetalPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    VisualizerMode.values().forEach { mode ->
                        ToggleSwitch(
                            checked = visualizerMode == mode,
                            onCheckedChange = { if (it) visualizerMode = mode },
                            label = mode.label
                        )
                    }
                }
            }
        }
    }
}

enum class VisualizerMode(val label: String) {
    OSCILLOSCOPE("Scope"),
    SPECTRUM("Spectrum"),
    STEREO("Stereo")
}

@Composable
fun OscilloscopeDisplay() {
    val infiniteTransition = rememberInfiniteTransition(label = "oscilloscope")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        ),
        label = "phase"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepShadow, RoundedCornerShape(8.dp))
            .border(3.dp, ChromeShadow, RoundedCornerShape(8.dp))
    ) {
        // CRT screen with grid
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .drawBehind {
                    // Grid lines (like oscilloscope graticule)
                    val gridColor = CRTGreen.copy(alpha = 0.15f)
                    val gridSpacing = size.width / 10
                    
                    // Vertical grid lines
                    for (i in 0..10) {
                        drawLine(
                            color = gridColor,
                            start = Offset(i * gridSpacing, 0f),
                            end = Offset(i * gridSpacing, size.height),
                            strokeWidth = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )
                    }
                    
                    // Horizontal grid lines
                    val vGridSpacing = size.height / 8
                    for (i in 0..8) {
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, i * vGridSpacing),
                            end = Offset(size.width, i * vGridSpacing),
                            strokeWidth = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )
                    }
                    
                    // Center crosshair
                    drawLine(
                        color = gridColor.copy(alpha = 0.3f),
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = gridColor.copy(alpha = 0.3f),
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 2f
                    )
                }
        ) {
            val path = Path()
            val points = 200
            val amplitude = size.height * 0.3f
            val centerY = size.height / 2
            
            // Generate waveform
            for (i in 0..points) {
                val x = (size.width / points) * i
                // Complex waveform: fundamental + harmonics
                val t = (i.toFloat() / points) * 4 * PI.toFloat() + phase
                val y = centerY + 
                    amplitude * 0.6f * sin(t) +  // Fundamental
                    amplitude * 0.2f * sin(t * 2) + // 2nd harmonic
                    amplitude * 0.1f * sin(t * 3)   // 3rd harmonic
                
                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            
            // Draw waveform with glow
            // Outer glow
            drawPath(
                path = path,
                color = CRTGreen.copy(alpha = 0.3f),
                style = Stroke(
                    width = 8f,
                    cap = StrokeCap.Round
                )
            )
            // Main trace
            drawPath(
                path = path,
                color = CRTGreen,
                style = Stroke(
                    width = 3f,
                    cap = StrokeCap.Round
                )
            )
        }
        
        // CRT screen glare overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.3f, size.height * 0.3f)
                    )
                )
        )
    }
}

@Composable
fun SpectrumAnalyzerDisplay() {
    val infiniteTransition = rememberInfiniteTransition(label = "spectrum")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing)
        ),
        label = "time"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepShadow, RoundedCornerShape(8.dp))
            .border(3.dp, ChromeShadow, RoundedCornerShape(8.dp))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val bands = 32
            val bandWidth = size.width / bands
            val maxHeight = size.height * 0.9f
            
            for (i in 0 until bands) {
                // Simulate frequency band levels
                val frequency = i.toFloat() / bands
                val level = (sin((time + i * 3) * 0.1) * 0.5 + 0.5) * 
                           (1f - frequency * 0.3f) // Lower frequencies have more energy
                
                val barHeight = maxHeight * level
                val x = i * bandWidth
                
                // Determine color based on level
                val color = when {
                    level > 0.8f -> VURed
                    level > 0.6f -> VUAmber
                    else -> VUGreen
                }
                
                // Bar with glow
                drawRect(
                    color = color.copy(alpha = 0.3f),
                    topLeft = Offset(x + 2, size.height - barHeight - 2),
                    size = androidx.compose.ui.geometry.Size(bandWidth - 4, barHeight)
                )
                drawRect(
                    color = color,
                    topLeft = Offset(x + 4, size.height - barHeight),
                    size = androidx.compose.ui.geometry.Size(bandWidth - 8, barHeight)
                )
            }
        }
    }
}

@Composable
fun StereoPhaseDisplay() {
    val infiniteTransition = rememberInfiniteTransition(label = "stereo phase")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        ),
        label = "angle"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepShadow, RoundedCornerShape(8.dp))
            .border(3.dp, ChromeShadow, RoundedCornerShape(8.dp))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = minOf(size.width, size.height) * 0.4f
            
            // Draw circular grid
            for (r in 1..4) {
                drawCircle(
                    color = LEDBlue.copy(alpha = 0.1f),
                    radius = radius * r / 4,
                    center = Offset(centerX, centerY),
                    style = Stroke(width = 1f)
                )
            }
            
            // Draw crosshair
            drawLine(
                color = LEDBlue.copy(alpha = 0.2f),
                start = Offset(centerX - radius, centerY),
                end = Offset(centerX + radius, centerY),
                strokeWidth = 1f
            )
            drawLine(
                color = LEDBlue.copy(alpha = 0.2f),
                start = Offset(centerX, centerY - radius),
                end = Offset(centerX, centerY + radius),
                strokeWidth = 1f
            )
            
            // Draw Lissajous pattern
            val path = Path()
            val points = 100
            
            for (i in 0..points) {
                val t = (i.toFloat() / points) * 2 * PI.toFloat() + angle
                val x = centerX + radius * 0.8f * sin(t)
                val y = centerY + radius * 0.8f * sin(t * 1.5f) // Different frequency for phase
                
                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            
            // Glow
            drawPath(
                path = path,
                color = LEDBlue.copy(alpha = 0.3f),
                style = Stroke(width = 8f, cap = StrokeCap.Round)
            )
            // Main trace
            drawPath(
                path = path,
                color = LEDBlue,
                style = Stroke(width = 3f, cap = StrokeCap.Round)
            )
        }
    }
}
