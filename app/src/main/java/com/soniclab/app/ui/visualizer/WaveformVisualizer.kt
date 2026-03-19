package com.soniclab.app.ui.visualizer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.soniclab.app.ui.theme.Cyan500
import com.soniclab.app.ui.theme.Amber500

@Composable
fun WaveformVisualizer(
    audioData: FloatArray,
    modifier: Modifier = Modifier,
    primaryColor: Color = Cyan500,
    secondaryColor: Color = Amber500
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerY = size.height / 2
        val width = size.width
        val barWidth = width / audioData.size
        
        audioData.forEachIndexed { index, amplitude ->
            val barHeight = (amplitude * size.height / 2).toFloat()
            val x = (index * barWidth).toFloat()
            
            val color = if (index % 2 == 0) primaryColor else secondaryColor
            
            drawRect(
                color = color,
                topLeft = Offset(x, centerY - barHeight / 2),
                size = Size(barWidth.toFloat(), barHeight)
            )
        }
    }
}

@Composable
fun LineWaveformVisualizer(
    audioData: FloatArray,
    modifier: Modifier = Modifier,
    color: Color = Cyan500
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerY = size.height / 2
        val width = size.width
        val stepWidth = width / audioData.size
        
        for (i in 0 until audioData.size - 1) {
            val x1 = (i * stepWidth).toFloat()
            val y1 = centerY + (audioData[i] * size.height / 2).toFloat()
            val x2 = ((i + 1) * stepWidth).toFloat()
            val y2 = centerY + (audioData[i + 1] * size.height / 2).toFloat()
            
            drawLine(
                color = color,
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = 3f
            )
        }
    }
}

@Composable
fun FrequencyBarsVisualizer(
    frequencies: FloatArray,
    modifier: Modifier = Modifier,
    color: Color = Amber500
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val barWidth = width / frequencies.size
        
        frequencies.forEachIndexed { index, magnitude ->
            val barHeight = (magnitude * size.height).toFloat()
            val x = (index * barWidth).toFloat()
            
            drawRect(
                color = color,
                topLeft = Offset(x, size.height - barHeight),
                size = Size(barWidth.toFloat(), barHeight)
            )
        }
    }
}
