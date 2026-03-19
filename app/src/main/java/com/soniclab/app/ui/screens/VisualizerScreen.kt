package com.soniclab.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VisualizerScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Visualizer content
        WaveformVisualization(
            audioData = FloatArray(50) { (it % 10) / 10f },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun WaveformVisualization(
    audioData: FloatArray,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerY = size.height / 2
        val width = size.width
        val barWidth = width / audioData.size
        
        audioData.forEachIndexed { index, amplitude ->
            val barHeight = (amplitude * size.height / 2).toFloat()
            val x = (index * barWidth).toFloat()
            
            drawRect(
                color = Color.Cyan,
                topLeft = Offset(x, centerY - barHeight / 2),
                size = Size(barWidth.toFloat(), barHeight)
            )
        }
    }
}

@Composable
fun CircularVisualizer(
    audioData: FloatArray,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(300.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension / 3
        
        audioData.forEachIndexed { index, value ->
            val angle = (index.toFloat() / audioData.size) * 2 * PI.toFloat()
            
            val radius = maxRadius + (value * maxRadius).toFloat()
            val x = center.x + cos(angle.toDouble()).toFloat() * radius
            val y = center.y + sin(angle.toDouble()).toFloat() * radius
            
            drawCircle(
                color = Color.Cyan,
                radius = 5f,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun VisualizerBox() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Blue)
    )
}
