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
