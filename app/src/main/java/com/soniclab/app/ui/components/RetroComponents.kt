package com.soniclab.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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
// 1. WOOD PANEL - Rich walnut background
// ============================================
@Composable
fun WoodPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .drawBehind {
                // Base wood tone
                drawRect(
                    brush = Brush.verticalGradient(
                        listOf(WalnutMid, WalnutDark, WalnutMid)
                    )
                )
                // Wood grain lines
                for (i in 0..20) {
                    val y = (size.height / 20) * i
                    drawLine(
                        color = WalnutDark.copy(alpha = 0.3f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                }
            }
    ) {
        content()
    }
}

// ============================================
// 2. BRUSHED METAL PANEL - Aluminum control panel
// ============================================
@Composable
fun BrushedMetalPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .drawBehind {
                // Brushed aluminum base
                drawRect(color = BrushedAluminum)
                
                // Vertical brush strokes
                for (i in 0..100) {
                    val x = (size.width / 100) * i
                    drawLine(
                        color = BrushedAluminumLight.copy(alpha = 0.2f),
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1f
                    )
                }
                
                // Chrome border
                drawLine(
                    color = ChromeHighlight,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 2f
                )
                drawLine(
                    color = ChromeShadow,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2f
                )
            }
            .padding(16.dp)
    ) {
        content()
    }
}

// ============================================
// 3. VU METER - Animated bouncy needle meter
// ============================================
@Composable
fun VUMeter(
    level: Float,  // 0.0 to 1.0
    label: String,
    modifier: Modifier = Modifier
) {
    // Spring animation for bouncy needle
    val animatedLevel by animateFloatAsState(
        targetValue = level,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "VU Meter Animation"
    )
    
    Box(
        modifier = modifier
            .width(120.dp)
            .height(100.dp)
            .background(WalnutDark)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height * 0.75f
            val radius = size.width * 0.4f
            
            // Meter arc (semicircle)
            drawArc(
                color = ChromeShadow,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(width = 3f)
            )
            
            // Scale markings
            for (i in 0..10) {
                val angle = (i / 10f) * 180f - 180f
                val angleRad = angle * PI.toFloat() / 180f
                val x1 = centerX + cos(angleRad) * radius
                val y1 = centerY + sin(angleRad) * radius
                val x2 = centerX + cos(angleRad) * (radius + 10f)
                val y2 = centerY + sin(angleRad) * (radius + 10f)
                
                val color = when {
                    i < 6 -> VUGreen
                    i < 9 -> VUAmber
                    else -> VURed
                }
                
                drawLine(
                    color = color,
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )
            }
            
            // Animated needle
            val needleAngle = (animatedLevel * 180f - 180f) * PI.toFloat() / 180f
            val needleX = centerX + cos(needleAngle) * (radius + 5f)
            val needleY = centerY + sin(needleAngle) * (radius + 5f)
            
            drawLine(
                color = NixieTubeOrange,
                start = Offset(centerX, centerY),
                end = Offset(needleX, needleY),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
        
        // Label
        Text(
            text = label,
            color = EngravingGold,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ============================================
// 4. ROTARY KNOB - Clickable aluminum knob
// ============================================
@Composable
fun RotaryKnob(
    value: Float,  // 0.0 to 1.0
    onValueChange: (Float) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clickable {
                    // Simple increment on click (full drag gesture in Phase 3)
                    val newValue = ((value + 0.1f) % 1.0f)
                    onValueChange(newValue)
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Knob body - radial gradient aluminum
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(
                            BrushedAluminumLight,
                            BrushedAluminum,
                            ChromeShadow
                        )
                    )
                )
                
                // Indicator line
                val angle = (value * 270f - 135f) * PI.toFloat() / 180f
                val lineLength = size.toPx() * 0.4f
                val centerX = this.size.width / 2
                val centerY = this.size.height / 2
                val endX = centerX + cos(angle) * lineLength
                val endY = centerY + sin(angle) * lineLength
                
                drawLine(
                    color = NixieTubeOrange,
                    start = Offset(centerX, centerY),
                    end = Offset(endX, endY),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            color = LabelGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

// ============================================
// 5. LED INDICATOR - Glowing LED with label
// ============================================
@Composable
fun LEDIndicator(
    isOn: Boolean,
    color: Color = LEDGreen,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    color = if (isOn) color else LEDOff,
                    shape = CircleShape
                )
                .drawBehind {
                    if (isOn) {
                        // Glow effect
                        drawCircle(
                            color = color.copy(alpha = 0.3f),
                            radius = size.minDimension * 0.8f
                        )
                        // Inner highlight
                        drawCircle(
                            color = Color.White.copy(alpha = 0.5f),
                            radius = size.minDimension * 0.3f,
                            center = Offset(size.width * 0.3f, size.height * 0.3f)
                        )
                    }
                }
        )
        
        Spacer(modifier = Modifier.height(2.dp))
        
        Text(
            text = label,
            color = LabelGray,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

// ============================================
// 6. NIXIE TUBE DISPLAY - Glowing orange digits
// ============================================
@Composable
fun NixieTubeDisplay(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .drawBehind {
                // Chrome border
                drawRect(
                    color = ChromeHighlight,
                    style = Stroke(width = 2f)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = NixieTubeOrange,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp,
            modifier = Modifier.drawBehind {
                // Warm glow effect
                drawRect(
                    color = TubeGlowWarm.copy(alpha = 0.2f)
                )
            }
        )
    }
}

// ============================================
// 7. TOGGLE SWITCH - Physical toggle aesthetic
// ============================================
@Composable
fun ToggleSwitch(
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
                .width(50.dp)
                .height(30.dp)
                .background(
                    color = if (checked) VUGreen.copy(alpha = 0.3f) else ChromeShadow,
                    shape = RoundedCornerShape(15.dp)
                )
                .clickable { onCheckedChange(!checked) }
                .padding(4.dp),
            contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(
                        color = if (checked) VUGreen else LabelGray,
                        shape = CircleShape
                    )
                    .drawBehind {
                        // Chrome highlight
                        drawCircle(
                            color = ChromeHighlight.copy(alpha = 0.5f),
                            radius = size.minDimension * 0.3f,
                            center = Offset(size.width * 0.3f, size.height * 0.3f)
                        )
                    }
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            color = LabelGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

// ============================================
// 8. TAPE REEL - Spinning reel animation
// ============================================
@Composable
fun TapeReel(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp
) {
    // Continuous rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "Tape Reel Rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation Animation"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = this.size.width / 2
            val centerY = this.size.height / 2
            val radius = this.size.minDimension / 2
            
            // Reel hub
            drawCircle(
                color = TapeReelBlack,
                radius = radius * 0.3f
            )
            
            // Reel rim
            drawCircle(
                color = TapeReelBrown,
                radius = radius,
                style = Stroke(width = 4f)
            )
            
            if (isPlaying) {
                // Rotating spokes
                rotate(rotation) {
                    for (i in 0..5) {
                        val angle = (i / 6f) * 360f * PI.toFloat() / 180f
                        val x = centerX + cos(angle) * radius * 0.8f
                        val y = centerY + sin(angle) * radius * 0.8f
                        
                        drawLine(
                            color = TapeReelSilver,
                            start = Offset(centerX, centerY),
                            end = Offset(x, y),
                            strokeWidth = 2f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}
