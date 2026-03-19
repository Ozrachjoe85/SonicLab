package com.soniclab.app.ui.visualizer

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.soniclab.app.ui.theme.Cyan500
import com.soniclab.app.ui.theme.Amber500
import kotlin.math.sin
import kotlin.math.PI

@Composable
fun WaveformVisualizer(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isPlaying) 3000 else 6000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )
    
    val amplitudePulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "amplitude"
    )
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f
        
        drawWaveLayer(
            width = width,
            centerY = centerY,
            phase = phase,
            amplitude = 60f * amplitudePulse,
            frequency = 0.015f,
            color = Cyan500,
            alpha = 0.3f
        )
        
        drawWaveLayer(
            width = width,
            centerY = centerY,
            phase = phase * 1.5f,
            amplitude = 40f * amplitudePulse,
            frequency = 0.02f,
            color = Amber500,
            alpha = 0.5f
        )
        
        drawWaveLayer(
            width = width,
            centerY = centerY,
            phase = phase * 0.7f,
            amplitude = 80f * amplitudePulse,
            frequency = 0.01f,
            color = Cyan500,
            alpha = 0.6f
        )
    }
}

private fun DrawScope.drawWaveLayer(
    width: Float,
    centerY: Float,
    phase: Float,
    amplitude: Float,
    frequency: Float,
    color: Color,
    alpha: Float
) {
    val path = Path()
    val points = 200
    
    path.moveTo(0f, centerY)
    
    for (i in 0..points) {
        val x = (i.toFloat() / points) * width
        val y = centerY + amplitude * sin(frequency * x + phase)
        path.lineTo(x, y)
    }
    
    drawPath(
        path = path,
        color = color.copy(alpha = alpha),
        style = Stroke(
            width = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    )
    
    drawPath(
        path = path,
        color = color.copy(alpha = alpha * 0.3f),
        style = Stroke(
            width = 8.dp.toPx(),
            cap = StrokeCap.Round
        )
    )
}
