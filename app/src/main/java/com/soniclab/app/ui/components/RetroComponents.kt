package com.soniclab.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// ============================================
// 1. COSMIC VOID PANEL - Deep space background with nebula
// ============================================
@Composable
fun CosmicVoidPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        CosmicVoid,
                        CosmicPurple,
                        NebulaRed,
                        CosmicBlue,
                        CosmicVoid
                    )
                )
            )
            .drawBehind {
                // Scattered stars
                for (i in 0..100) {
                    val x = (size.width * (i * 47 % 100) / 100f)
                    val y = (size.height * (i * 83 % 100) / 100f)
                    val starSize = ((i % 3) + 1).toFloat()
                    
                    drawCircle(
                        color = Color.White.copy(alpha = 0.3f + (i % 5) * 0.1f),
                        radius = starSize,
                        center = Offset(x, y)
                    )
                }
            }
    ) {
        content()
    }
}

// ============================================
// 2. DIGITAL GRID PANEL - Cyberpunk wireframe overlay
// ============================================
@Composable
fun DigitalGridPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(BgPanel.copy(alpha = 0.7f))
            .border(2.dp, GridCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .drawBehind {
                // Grid lines
                val gridSpacing = 30f
                
                // Vertical lines
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
                
                // Horizontal lines
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
                
                // Corner accents
                val cornerSize = 20f
                // Top-left
                drawLine(GridPurple, Offset(0f, 0f), Offset(cornerSize, 0f), 3f)
                drawLine(GridPurple, Offset(0f, 0f), Offset(0f, cornerSize), 3f)
                // Top-right
                drawLine(GridPurple, Offset(size.width - cornerSize, 0f), Offset(size.width, 0f), 3f)
                drawLine(GridPurple, Offset(size.width, 0f), Offset(size.width, cornerSize), 3f)
            }
            .padding(16.dp)
    ) {
        content()
    }
}

// ============================================
// 3. HOLOGRAPHIC VU METER - Glowing arc with energy pulse
// ============================================
@Composable
fun HolographicVUMeter(
    level: Float,
    label: String,
    modifier: Modifier = Modifier
) {
    val animatedLevel by animateFloatAsState(
        targetValue = level,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Holographic VU"
    )
    
    // Pulsing glow effect
    val infiniteTransition = rememberInfiniteTransition(label = "VU Pulse")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Glow Pulse"
    )
    
    Box(
        modifier = modifier
            .width(120.dp)
            .height(120.dp)
            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .border(1.dp, NeonCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height * 0.7f
            val radius = size.width * 0.35f
            
            // Outer glow arc
            drawArc(
                brush = Brush.radialGradient(
                    listOf(
                        NeonCyan.copy(alpha = glowPulse * 0.3f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY),
                    radius = radius * 1.5f
                ),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(width = 15f)
            )
            
            // Main arc with color gradient
            for (i in 0..100) {
                val progress = i / 100f
                val angle = 180f + progress * 180f
                
                val color = when {
                    progress < 0.6f -> VUCosmicLow
                    progress < 0.85f -> VUCosmicMid
                    progress < 0.95f -> VUCosmicHigh
                    else -> VUCosmicPeak
                }
                
                if (progress <= animatedLevel) {
                    drawArc(
                        color = color,
                        startAngle = angle - 1f,
                        sweepAngle = 2f,
                        useCenter = false,
                        style = Stroke(width = 8f, cap = StrokeCap.Round)
                    )
                }
            }
            
            // Energy needle
            val needleAngle = (animatedLevel * 180f + 180f) * PI.toFloat() / 180f
            val needleLength = radius * 1.2f
            val needleX = centerX + cos(needleAngle) * needleLength
            val needleY = centerY + sin(needleAngle) * needleLength
            
            // Needle glow
            drawLine(
                brush = Brush.linearGradient(
                    listOf(
                        PlasmaViolet.copy(alpha = 0.8f),
                        NeonPink
                    )
                ),
                start = Offset(centerX, centerY),
                end = Offset(needleX, needleY),
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )
            
            // Center energy core
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(EnergyCore, EnergyPulse, Color.Transparent)
                ),
                radius = 8f,
                center = Offset(centerX, centerY)
            )
        }
        
        Text(
            text = label,
            color = GridCyan,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ============================================
// 4. PLASMA KNOB - Energy-filled control
// ============================================
@Composable
fun PlasmaKnob(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    size: Dp = 70.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Plasma Flow")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Plasma Rotation"
    )
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clickable {
                    val newValue = ((value + 0.1f) % 1.0f)
                    onValueChange(newValue)
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Plasma background (rotating)
                rotate(rotation) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(
                                PlasmaViolet,
                                PlasmaMagenta,
                                PlasmaBlue,
                                PlasmaCyan
                            )
                        )
                    )
                }
                
                // Outer ring
                drawCircle(
                    color = NeonCyan,
                    style = Stroke(width = 3f)
                )
                
                // Value indicator
                val angle = (value * 270f - 135f) * PI.toFloat() / 180f
                val lineLength = this.size.minDimension * 0.4f
                val centerX = this.size.width / 2
                val centerY = this.size.height / 2
                val endX = centerX + cos(angle) * lineLength
                val endY = centerY + sin(angle) * lineLength
                
                // Indicator with glow
                drawLine(
                    brush = Brush.linearGradient(
                        listOf(EnergyCore, NeonPink)
                    ),
                    start = Offset(centerX, centerY),
                    end = Offset(endX, endY),
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
                
                // Center dot
                drawCircle(
                    color = EnergyCore,
                    radius = 4f
                )
            }
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
    }
}

// ============================================
// 5. NEON LED - Glowing cyberpunk indicator
// ============================================
@Composable
fun NeonLED(
    isOn: Boolean,
    color: Color = NeonCyan,
    label: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LED Pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse"
    )
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer glow
            if (isOn) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color.copy(alpha = pulse * 0.3f),
                            CircleShape
                        )
                        .blur(8.dp)
                )
            }
            
            // LED itself
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        if (isOn) color else Color(0xFF222222),
                        CircleShape
                    )
                    .border(
                        1.dp,
                        if (isOn) color.copy(alpha = 0.8f) else Color(0xFF444444),
                        CircleShape
                    )
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            color = if (isOn) color.copy(alpha = 0.8f) else Gray300,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

// ============================================
// 6. HOLOGRAPHIC DISPLAY - Glowing data readout
// ============================================
@Composable
fun HolographicDisplay(
    text: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Holo Shimmer")
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Shimmer"
    )
    
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
            .border(
                2.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        HoloBlue.copy(alpha = 0.5f + shimmer * 0.5f),
                        HoloPink.copy(alpha = 0.5f + (1f - shimmer) * 0.5f),
                        HoloBlue.copy(alpha = 0.5f + shimmer * 0.5f)
                    )
                ),
                RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        // Scanline effect
        Canvas(modifier = Modifier.matchParentSize()) {
            for (i in 0..20) {
                val y = (size.height / 20) * i
                drawLine(
                    color = ScanlineLight.copy(alpha = 0.1f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
            }
        }
        
        Text(
            text = text,
            color = NeonCyan,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 6.sp,
            modifier = Modifier.drawBehind {
                // Text glow
                drawRect(
                    brush = Brush.radialGradient(
                        listOf(
                            NeonCyan.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
            }
        )
    }
}

// ============================================
// 7. ENERGY SWITCH - Plasma-filled toggle
// ============================================
@Composable
fun EnergySwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(32.dp)
                .background(
                    brush = if (checked) {
                        Brush.horizontalGradient(listOf(PlasmaViolet, PlasmaCyan))
                    } else {
                        Brush.horizontalGradient(listOf(Color(0xFF222222), Color(0xFF111111)))
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    2.dp,
                    if (checked) NeonCyan else Color(0xFF444444),
                    RoundedCornerShape(16.dp)
                )
                .clickable { onCheckedChange(!checked) }
                .padding(4.dp),
            contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        if (checked) EnergyCore else Color(0xFF666666),
                        CircleShape
                    )
                    .border(
                        2.dp,
                        if (checked) NeonPink else Color.Transparent,
                        CircleShape
                    )
            )
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = label,
            color = if (checked) TextSecondary else Gray300,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
    }
}

// ============================================
// 8. DATA STREAM REEL - Flowing energy visualization
// ============================================
@Composable
fun DataStreamReel(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Data Stream")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Stream Rotation"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = this.size.width / 2
            val centerY = this.size.height / 2
            val radius = this.size.minDimension / 2
            
            // Outer ring
            drawCircle(
                color = NeonCyan.copy(alpha = 0.3f),
                radius = radius,
                style = Stroke(width = 2f)
            )
            
            if (isActive) {
                // Rotating energy streams
                rotate(rotation) {
                    for (i in 0..7) {
                        val angle = (i / 8f) * 360f * PI.toFloat() / 180f
                        val x1 = centerX + cos(angle) * (radius * 0.3f)
                        val y1 = centerY + sin(angle) * (radius * 0.3f)
                        val x2 = centerX + cos(angle) * (radius * 0.9f)
                        val y2 = centerY + sin(angle) * (radius * 0.9f)
                        
                        drawLine(
                            brush = Brush.linearGradient(
                                listOf(
                                    PlasmaViolet,
                                    PlasmaCyan,
                                    Color.Transparent
                                )
                            ),
                            start = Offset(x1, y1),
                            end = Offset(x2, y2),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
            
            // Center core
            drawCircle(
                brush = Brush.radialGradient(
                    if (isActive) {
                        listOf(EnergyCore, EnergyPulse, PlasmaViolet, Color.Transparent)
                    } else {
                        listOf(Color(0xFF444444), Color.Transparent)
                    }
                ),
                radius = radius * 0.3f
            )
        }
    }
}
