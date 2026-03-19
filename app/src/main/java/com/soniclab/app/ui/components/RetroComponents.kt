package com.soniclab.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

/**
 * Wood grain panel background - realistic texture
 */
@Composable
fun WoodPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        WalnutMid,
                        WalnutDark,
                        WalnutMid.copy(alpha = 0.9f)
                    )
                )
            )
            .drawBehind {
                // Subtle wood grain lines
                for (i in 0..30) {
                    val y = (size.height / 30) * i
                    drawLine(
                        color = if (i % 3 == 0) WalnutLight.copy(alpha = 0.1f) 
                               else WalnutDark.copy(alpha = 0.05f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = if (i % 3 == 0) 2f else 1f
                    )
                }
            }
    ) {
        content()
    }
}

/**
 * Brushed aluminum panel with directional highlights
 */
@Composable
fun BrushedMetalPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        BrushedAluminumLight,
                        BrushedAluminum,
                        ChromeShadow,
                        BrushedAluminum,
                        BrushedAluminumLight
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
            .drawBehind {
                // Brushed texture lines
                for (i in 0..100) {
                    val x = (size.width / 100) * i
                    drawLine(
                        color = ChromeHighlight.copy(alpha = 0.03f),
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 0.5f
                    )
                }
            }
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(ChromeHighlight, ChromeShadow)
                ),
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        content()
    }
}

/**
 * Vintage VU Meter with animated needle
 */
@Composable
fun VUMeter(
    level: Float, // 0.0 to 1.0
    modifier: Modifier = Modifier,
    label: String = "LEVEL"
) {
    // Animate the needle smoothly
    val animatedLevel by animateFloatAsState(
        targetValue = level.coerceIn(0f, 1f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "VU Needle"
    )
    
    Box(
        modifier = modifier
            .aspectRatio(2f)
            .background(PanelInset, RoundedCornerShape(8.dp))
            .border(1.dp, ChromeShadow, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height * 0.8f
            val radius = size.height * 0.7f
            
            // Draw scale arc background
            drawArc(
                color = DeepShadow,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx()),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(centerX - radius, centerY - radius)
            )
            
            // Draw colored scale sections
            val sections = listOf(
                Triple(0f, 60f, VUGreen),      // Green zone
                Triple(60f, 30f, VUAmber),      // Amber zone
                Triple(90f, 90f, VURed)         // Red zone
            )
            
            sections.forEach { (start, sweep, color) ->
                drawArc(
                    color = color.copy(alpha = 0.3f),
                    startAngle = 180f + start,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx()),
                    size = Size(radius * 2, radius * 2),
                    topLeft = Offset(centerX - radius, centerY - radius)
                )
            }
            
            // Draw scale markings
            for (i in 0..10) {
                val angle = (180 + (i * 18)).toDouble() * PI / 180
                val startRadius = radius - 20f
                val endRadius = radius - 5f
                
                drawLine(
                    color = LabelGray,
                    start = Offset(
                        centerX + (cos(angle) * startRadius).toFloat(),
                        centerY + (sin(angle) * startRadius).toFloat()
                    ),
                    end = Offset(
                        centerX + (cos(angle) * endRadius).toFloat(),
                        centerY + (sin(angle) * endRadius).toFloat()
                    ),
                    strokeWidth = 2.dp.toPx()
                )
            }
            
            // Draw needle with glow
            val needleAngle = 180.0 + (animatedLevel * 180.0)
            val needleRadians = needleAngle * PI / 180
            val needleLength = radius - 15f
            
            // Needle glow
            drawLine(
                color = NixieTubeOrange.copy(alpha = 0.5f),
                start = Offset(centerX, centerY),
                end = Offset(
                    centerX + (cos(needleRadians) * needleLength).toFloat(),
                    centerY + (sin(needleRadians) * needleLength).toFloat()
                ),
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round
            )
            
            // Needle body
            drawLine(
                color = ChromeHighlight,
                start = Offset(centerX, centerY),
                end = Offset(
                    centerX + (cos(needleRadians) * needleLength).toFloat(),
                    centerY + (sin(needleRadians) * needleLength).toFloat()
                ),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            
            // Center pivot
            drawCircle(
                color = ChromeShadow,
                radius = 8.dp.toPx(),
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = BrushedAluminum,
                radius = 5.dp.toPx(),
                center = Offset(centerX, centerY)
            )
        }
        
        // Label at top
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = EngravingGold,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 4.dp)
        )
    }
}

/**
 * Vintage rotary knob with indicator line
 */
@Composable
fun RotaryKnob(
    value: Float, // 0.0 to 1.0
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    size: Dp = 64.dp
) {
    var localValue by remember { mutableStateOf(value) }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Knob
        Box(
            modifier = Modifier
                .size(size)
                .shadow(4.dp, CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            BrushedAluminumLight,
                            BrushedAluminum,
                            ChromeShadow
                        )
                    ),
                    shape = CircleShape
                )
                .border(2.dp, ChromeShadow, CircleShape)
                .clickable {
                    // Simple click to increment (real app would use drag)
                    val newValue = (localValue + 0.1f).coerceIn(0f, 1f)
                    localValue = newValue
                    onValueChange(newValue)
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val angle = -135f + (localValue * 270f) // -135° to +135°
                
                rotate(angle, Offset(centerX, centerY)) {
                    // Indicator line
                    drawLine(
                        color = NixieTubeOrange,
                        start = Offset(centerX, centerY * 0.3f),
                        end = Offset(centerX, centerY * 0.6f),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                
                // Center dot
                drawCircle(
                    color = PanelInset,
                    radius = 6.dp.toPx(),
                    center = Offset(centerX, centerY)
                )
            }
        }
        
        // Label
        if (label.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = LabelGray,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

/**
 * LED indicator with glow effect
 */
@Composable
fun LEDIndicator(
    isOn: Boolean,
    color: Color = LEDGreen,
    modifier: Modifier = Modifier,
    label: String = ""
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .shadow(
                    elevation = if (isOn) 8.dp else 0.dp,
                    shape = CircleShape,
                    spotColor = color
                )
                .background(
                    color = if (isOn) color else color.copy(alpha = 0.2f),
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = ChromeShadow,
                    shape = CircleShape
                )
        ) {
            // Inner glow
            if (isOn) {
                Box(
