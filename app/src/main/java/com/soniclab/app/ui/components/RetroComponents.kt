package com.soniclab.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RetroButton(
    modifier: Modifier = Modifier,
    size: IntSize = IntSize(100, 40)
) {
    Box(
        modifier = modifier
            .width(size.width.dp)
            .height(size.height.dp)
    )
}

@Composable
fun RetroIcon(
    iconSize: Int = 24,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.PlayArrow,
        contentDescription = "Play",
        modifier = modifier
            .width(iconSize.dp)
            .height(iconSize.dp)
    )
}

@Composable
fun RetroCircleIndicator(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(200.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.width / 3
        
        for (i in 0..11) {
            val angle = i * 30.0 * PI / 180.0
            val x = centerX + cos(angle).toFloat() * radius
            val y = centerY + sin(angle).toFloat() * radius
            
            drawCircle(
                color = Color.Cyan,
                radius = 10f,
                center = Offset(x, y)
            )
        }
    }
}
