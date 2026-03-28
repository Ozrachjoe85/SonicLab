package com.soniclab.app.ui.screens

import android.media.audiofx.Visualizer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.soniclab.app.playback.PlayerManager
import com.soniclab.app.ui.theme.sonicColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.math.*

@HiltViewModel
class VisualizerViewModel @Inject constructor(
    private val playerManager: PlayerManager
) : ViewModel() {
    
    private var visualizer: Visualizer? = null
    private val _waveform = MutableStateFlow(ByteArray(0))
    val waveform: StateFlow<ByteArray> = _waveform
    
    private val _fft = MutableStateFlow(ByteArray(0))
    val fft: StateFlow<ByteArray> = _fft
    
    private val _selectedVisualizer = MutableStateFlow(VisualizerType.SPECTRUM)
    val selectedVisualizer: StateFlow<VisualizerType> = _selectedVisualizer
    
    init {
        initializeVisualizer()
    }
    
    private fun initializeVisualizer() {
        try {
            val audioSessionId = playerManager.getAudioSessionId()
            if (audioSessionId != 0) {
                visualizer = Visualizer(audioSessionId).apply {
                    captureSize = Visualizer.getCaptureSizeRange()[1]
                    setDataCaptureListener(
                        object : Visualizer.OnDataCaptureListener {
                            override fun onWaveFormDataCapture(visualizer: Visualizer?, waveform: ByteArray?, samplingRate: Int) {
                                waveform?.let { _waveform.value = it }
                            }
                            override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {
                                fft?.let { _fft.value = it }
                            }
                        },
                        Visualizer.getMaxCaptureRate(), true, true
                    )
                    enabled = true
                }
            }
        } catch (e: Exception) {
            // Visualizer not supported
        }
    }
    
    fun setVisualizer(type: VisualizerType) {
        _selectedVisualizer.value = type
    }
    
    override fun onCleared() {
        visualizer?.release()
        super.onCleared()
    }
}

enum class VisualizerType {
    SPECTRUM, WAVEFORM, RADIAL, PARTICLES, VU_METER, LISSAJOUS, GRID, VOID
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualsScreen(
    modifier: Modifier = Modifier,
    viewModel: VisualizerViewModel = hiltViewModel()
) {
    val colors = sonicColors
    val selectedType by viewModel.selectedVisualizer.collectAsState()
    val fft by viewModel.fft.collectAsState()
    val waveform by viewModel.waveform.collectAsState()
    
    val visualizers = listOf(
        VisualizerType.SPECTRUM to "Spectrum",
        VisualizerType.WAVEFORM to "Waveform",
        VisualizerType.RADIAL to "Radial",
        VisualizerType.PARTICLES to "Particles",
        VisualizerType.VU_METER to "VU Meter",
        VisualizerType.LISSAJOUS to "Lissajous",
        VisualizerType.GRID to "Grid",
        VisualizerType.VOID to "Void"
    )
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Visualizer selector
        ScrollableTabRow(
            selectedTabIndex = visualizers.indexOfFirst { it.first == selectedType },
            containerColor = colors.surface,
            contentColor = colors.primary,
            edgePadding = 16.dp
        ) {
            visualizers.forEachIndexed { index, (type, name) ->
                Tab(
                    selected = selectedType == type,
                    onClick = { viewModel.setVisualizer(type) },
                    text = { Text(name) }
                )
            }
        }
        
        // Visualizer display
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            when (selectedType) {
                VisualizerType.SPECTRUM -> SpectrumVisualizer(fft)
                VisualizerType.WAVEFORM -> WaveformVisualizer(waveform)
                VisualizerType.RADIAL -> RadialVisualizer(fft)
                VisualizerType.PARTICLES -> ParticleVisualizer(fft)
                VisualizerType.VU_METER -> VUMeterVisualizer(waveform)
                VisualizerType.LISSAJOUS -> LissajousVisualizer(waveform)
                VisualizerType.GRID -> GridVisualizer(fft)
                VisualizerType.VOID -> VoidVisualizer(fft)
            }
        }
    }
}

@Composable
fun SpectrumVisualizer(fft: ByteArray) {
    val colors = sonicColors
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (fft.isEmpty()) return@Canvas
        val barCount = min(64, fft.size / 2)
        val barWidth = size.width / barCount
        
        for (i in 0 until barCount) {
            val idx = i * 2
            if (idx + 1 >= fft.size) break
            val real = fft[idx].toFloat()
            val imag = fft[idx + 1].toFloat()
            val magnitude = sqrt(real * real + imag * imag)
            val normalizedHeight = (magnitude / 128f).coerceIn(0f, 1f)
            val barHeight = size.height * normalizedHeight
            
            val gradient = Brush.verticalGradient(
                colors = listOf(colors.primary, colors.secondary),
                startY = size.height,
                endY = size.height - barHeight
            )
            
            drawRect(
                brush = gradient,
                topLeft = Offset(i * barWidth, size.height - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth * 0.9f, barHeight)
            )
        }
    }
}

@Composable
fun WaveformVisualizer(waveform: ByteArray) {
    val colors = sonicColors
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (waveform.isEmpty()) return@Canvas
        val path = Path()
        val step = size.width / waveform.size
        path.moveTo(0f, size.height / 2)
        
        waveform.forEachIndexed { index, byte ->
            val x = index * step
            val y = size.height / 2 + (byte.toInt() / 128f) * size.height / 2
            path.lineTo(x, y)
        }
        
        drawPath(path, colors.primary, style = Stroke(width = 3f))
    }
}

@Composable
fun RadialVisualizer(fft: ByteArray) {
    val colors = sonicColors
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (fft.isEmpty()) return@Canvas
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxRadius = min(centerX, centerY) * 0.8f
        val barCount = min(64, fft.size / 2)
        
        for (i in 0 until barCount) {
            val idx = i * 2
            if (idx + 1 >= fft.size) break
            val magnitude = sqrt(fft[idx] * fft[idx] + fft[idx + 1] * fft[idx + 1].toFloat())
            val normalizedMag = (magnitude / 128f).coerceIn(0f, 1f)
            val angle = (i.toFloat() / barCount) * 2 * PI.toFloat()
            val radius = maxRadius * (0.5f + normalizedMag * 0.5f)
            
            val x = centerX + cos(angle) * radius
            val y = centerY + sin(angle) * radius
            
            drawCircle(colors.primary, 4f, Offset(x, y))
        }
    }
}

@Composable
fun ParticleVisualizer(fft: ByteArray) {
    val colors = sonicColors
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (fft.isEmpty()) return@Canvas
        repeat(100) { i ->
            val idx = (i * 2) % fft.size
            val magnitude = if (idx + 1 < fft.size) {
                sqrt(fft[idx] * fft[idx] + fft[idx + 1] * fft[idx + 1].toFloat())
            } else 0f
            val x = (i % 10) * size.width / 10
            val y = (i / 10) * size.height / 10
            val radius = (magnitude / 128f) * 20f
            drawCircle(colors.primary.copy(alpha = 0.5f), radius, Offset(x, y))
        }
    }
}

@Composable
fun VUMeterVisualizer(waveform: ByteArray) {
    val colors = sonicColors
    val level = if (waveform.isNotEmpty()) {
        waveform.map { abs(it.toInt()) }.average().toFloat() / 128f
    } else 0f
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val meterWidth = size.width * 0.8f
        val meterHeight = size.height * 0.2f
        val startX = (size.width - meterWidth) / 2
        val startY = (size.height - meterHeight) / 2
        
        drawRect(
            Color.Gray.copy(alpha = 0.3f),
            topLeft = Offset(startX, startY),
            size = androidx.compose.ui.geometry.Size(meterWidth, meterHeight)
        )
        
        val fillWidth = meterWidth * level
        drawRect(
            colors.primary,
            topLeft = Offset(startX, startY),
            size = androidx.compose.ui.geometry.Size(fillWidth, meterHeight)
        )
    }
}

@Composable
fun LissajousVisualizer(waveform: ByteArray) {
    val colors = sonicColors
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (waveform.size < 2) return@Canvas
        val path = Path()
        val halfSize = waveform.size / 2
        
        for (i in 0 until halfSize) {
            val x = size.width / 2 + (waveform[i].toInt() / 128f) * size.width / 2
            val y = size.height / 2 + (waveform[halfSize + i].toInt() / 128f) * size.height / 2
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        
        drawPath(path, colors.primary, style = Stroke(width = 2f))
    }
}

@Composable
fun GridVisualizer(fft: ByteArray) {
    val colors = sonicColors
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (fft.isEmpty()) return@Canvas
        val cols = 8
        val rows = 8
        val cellWidth = size.width / cols
        val cellHeight = size.height / rows
        
        repeat(cols * rows) { i ->
            val idx = (i * 2) % fft.size
            val magnitude = if (idx + 1 < fft.size) {
                sqrt(fft[idx] * fft[idx] + fft[idx + 1] * fft[idx + 1].toFloat())
            } else 0f
            val col = i % cols
            val row = i / cols
            val alpha = (magnitude / 128f).coerceIn(0f, 1f)
            
            drawRect(
                colors.primary.copy(alpha = alpha),
                topLeft = Offset(col * cellWidth, row * cellHeight),
                size = androidx.compose.ui.geometry.Size(cellWidth * 0.9f, cellHeight * 0.9f)
            )
        }
    }
}

@Composable
fun VoidVisualizer(fft: ByteArray) {
    val colors = sonicColors
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (fft.isEmpty()) return@Canvas
        val centerX = size.width / 2
        val centerY = size.height / 2
        
        repeat(20) { i ->
            val idx = (i * 4) % fft.size
            val magnitude = if (idx + 1 < fft.size) {
                sqrt(fft[idx] * fft[idx] + fft[idx + 1] * fft[idx + 1].toFloat())
            } else 0f
            val radius = (magnitude / 128f) * min(centerX, centerY)
            
            drawCircle(
                colors.primary.copy(alpha = 0.1f),
                radius,
                Offset(centerX, centerY),
                style = Stroke(width = 2f)
            )
        }
    }
}
