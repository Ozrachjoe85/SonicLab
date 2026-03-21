package com.soniclab.app.ui.screens

import android.media.audiofx.Equalizer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soniclab.app.playback.PlayerManager
import com.soniclab.app.ui.theme.sonicColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EQBand(
    val frequency: String,
    val level: Float
)

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    private val playerManager: PlayerManager
) : ViewModel() {
    
    private var equalizer: Equalizer? = null
    
    private val _isEnabled = MutableStateFlow(false)
    val isEnabled: StateFlow<Boolean> = _isEnabled.asStateFlow()
    
    private val _bands = MutableStateFlow<List<EQBand>>(emptyList())
    val bands: StateFlow<List<EQBand>> = _bands.asStateFlow()
    
    private val _selectedPreset = MutableStateFlow("Flat")
    val selectedPreset: StateFlow<String> = _selectedPreset.asStateFlow()
    
    init {
        initializeEqualizer()
    }
    
    private fun initializeEqualizer() {
        try {
            val audioSessionId = playerManager.getPlayer().audioSessionId
            equalizer = Equalizer(0, audioSessionId).apply {
                enabled = false
            }
            
            loadBands()
        } catch (e: Exception) {
            // EQ not supported
        }
    }
    
    private fun loadBands() {
        equalizer?.let { eq ->
            val numBands = eq.numberOfBands.toInt()
            val bandList = mutableListOf<EQBand>()
            
            for (i in 0 until numBands) {
                val freq = eq.getCenterFreq(i.toShort())
                val freqStr = when {
                    freq < 1000 -> "${freq}Hz"
                    else -> "${freq / 1000}kHz"
                }
                bandList.add(EQBand(freqStr, 0f))
            }
            
            _bands.value = bandList
        }
    }
    
    fun toggleEqualizer() {
        _isEnabled.value = !_isEnabled.value
        equalizer?.enabled = _isEnabled.value
    }
    
    fun setBandLevel(bandIndex: Int, level: Float) {
        equalizer?.let { eq ->
            val minLevel = eq.bandLevelRange[0]
            val maxLevel = eq.bandLevelRange[1]
            val actualLevel = (level * (maxLevel - minLevel) + minLevel).toInt().toShort()
            
            eq.setBandLevel(bandIndex.toShort(), actualLevel)
            
            val updated = _bands.value.toMutableList()
            updated[bandIndex] = updated[bandIndex].copy(level = level)
            _bands.value = updated
        }
    }
    
    fun applyPreset(preset: String) {
        _selectedPreset.value = preset
        
        val levels = when (preset) {
            "Flat" -> List(10) { 0f }
            "Bass Boost" -> listOf(0.8f, 0.7f, 0.5f, 0.2f, 0f, 0f, 0f, 0f, 0f, 0f)
            "Treble Boost" -> listOf(0f, 0f, 0f, 0f, 0f, 0.2f, 0.5f, 0.7f, 0.8f, 0.9f)
            "Vocal Boost" -> listOf(0f, 0f, 0.3f, 0.5f, 0.6f, 0.5f, 0.3f, 0f, 0f, 0f)
            "Rock" -> listOf(0.6f, 0.4f, 0.2f, 0f, -0.2f, -0.1f, 0.1f, 0.3f, 0.5f, 0.6f)
            "Pop" -> listOf(0f, 0.3f, 0.5f, 0.4f, 0.2f, 0f, -0.2f, -0.2f, 0f, 0.2f)
            "Classical" -> listOf(0f, 0f, 0f, 0f, 0f, 0f, -0.3f, -0.3f, -0.3f, -0.4f)
            "Jazz" -> listOf(0f, 0f, 0f, 0.3f, 0.4f, 0.4f, 0f, 0.2f, 0.3f, 0.4f)
            else -> List(10) { 0f }
        }
        
        levels.forEachIndexed { index, level ->
            if (index < _bands.value.size) {
                setBandLevel(index, level)
            }
        }
    }
    
    override fun onCleared() {
        equalizer?.release()
        super.onCleared()
    }
}

@Composable
fun EqualizerScreen(
    modifier: Modifier = Modifier,
    viewModel: EqualizerViewModel = hiltViewModel()
) {
    val colors = sonicColors
    val scrollState = rememberScrollState()
    
    val isEnabled by viewModel.isEnabled.collectAsState()
    val bands by viewModel.bands.collectAsState()
    val selectedPreset by viewModel.selectedPreset.collectAsState()
    
    val presets = listOf("Flat", "Bass Boost", "Treble Boost", "Vocal Boost", "Rock", "Pop", "Classical", "Jazz")
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(scrollState)
            .padding(24.dp)
            .padding(bottom = 80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "EQUALIZER",
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            
            Switch(
                checked = isEnabled,
                onCheckedChange = { viewModel.toggleEqualizer() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colors.primary,
                    checkedTrackColor = colors.primary.copy(alpha = 0.5f)
                )
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "PRESETS",
            color = colors.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                presets.chunked(4).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { preset ->
                            FilterChip(
                                selected = selectedPreset == preset,
                                onClick = { viewModel.applyPreset(preset) },
                                label = { Text(preset, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colors.primary.copy(alpha = 0.2f),
                                    selectedLabelColor = colors.primary
                                )
                            )
                        }
                    }
                    if (row != presets.chunked(4).last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "FREQUENCY BANDS",
            color = colors.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (bands.isEmpty()) {
                    Text(
                        "Equalizer not supported on this device",
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )
                } else {
                    bands.forEachIndexed { index, band ->
                        EQBandSlider(
                            frequency = band.frequency,
                            level = band.level,
                            enabled = isEnabled,
                            onLevelChange = { viewModel.setBandLevel(index, it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EQBandSlider(
    frequency: String,
    level: Float,
    enabled: Boolean,
    onLevelChange: (Float) -> Unit
) {
    val colors = sonicColors
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = frequency,
                color = if (enabled) colors.textPrimary else colors.textTertiary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = when {
                    level > 0 -> "+${(level * 10).toInt()}"
                    level < 0 -> "${(level * 10).toInt()}"
                    else -> "0"
                },
                color = if (enabled) colors.primary else colors.textTertiary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Slider(
            value = (level + 1f) / 2f,
            onValueChange = { onLevelChange(it * 2f - 1f) },
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = colors.primary,
                activeTrackColor = colors.primary,
                inactiveTrackColor = colors.border,
                disabledThumbColor = colors.textTertiary,
                disabledActiveTrackColor = colors.border
            )
        )
    }
}
