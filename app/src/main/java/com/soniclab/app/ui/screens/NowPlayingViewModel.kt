package com.soniclab.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soniclab.app.playback.MusicScanner
import com.soniclab.app.playback.PlayerManager
import com.soniclab.app.playback.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * NowPlayingViewModel - Connects UI with PlayerManager
 */
@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val playerManager: PlayerManager,
    private val musicScanner: MusicScanner
) : ViewModel() {
    
    val isPlaying: StateFlow<Boolean> = playerManager.isPlaying
    val currentTrack: StateFlow<Track?> = playerManager.currentTrack
    
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()
    
    private val _currentPosition = MutableStateFlow("0:00")
    val currentPosition: StateFlow<String> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow("0:00")
    val duration: StateFlow<String> = _duration.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private var progressJob: Job? = null
    
    init {
        playerManager.initialize()
        startProgressTracking()
        loadMusicFiles()
    }
    
    private fun loadMusicFiles() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val tracks = musicScanner.scanMusicFiles()
                
                if (tracks.isNotEmpty()) {
                    playerManager.setQueueAndPlay(tracks, 0)
                    playerManager.pause()
                } else {
                    _errorMessage.value = "No music files found on device"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading music: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive) {
                val position = playerManager.getCurrentPosition()
                val duration = playerManager.getDuration()
                
                if (duration > 0) {
                    _progress.value = position.toFloat() / duration.toFloat()
                    _currentPosition.value = formatTime(position)
                    _duration.value = formatTime(duration)
                }
                
                delay(100)
            }
        }
    }
    
    private fun formatTime(ms: Long): String {
        val seconds = (ms / 1000).toInt()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%d:%02d", minutes, remainingSeconds)
    }
    
    fun togglePlayPause() {
        playerManager.togglePlayPause()
    }
    
    fun playNext() {
        playerManager.playNext()
    }
    
    fun playPrevious() {
        playerManager.playPrevious()
    }
    
    fun seekTo(progress: Float) {
        val duration = playerManager.getDuration()
        val position = (duration * progress).toLong()
        playerManager.seekTo(position)
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    override fun onCleared() {
        super.onCleared()
        progressJob?.cancel()
    }
}
