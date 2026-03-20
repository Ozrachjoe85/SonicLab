package com.soniclab.app.ui.screens

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
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
 * NowPlayingViewModel - Enhanced with permission checking
 */
@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val playerManager: PlayerManager,
    private val musicScanner: MusicScanner,
    application: Application
) : AndroidViewModel(application) {
    
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
    
    private val _trackCount = MutableStateFlow(0)
    val trackCount: StateFlow<Int> = _trackCount.asStateFlow()
    
    private var progressJob: Job? = null
    
    init {
        playerManager.initialize()
        startProgressTracking()
        checkPermissionsAndLoad()
    }
    
    private fun checkPermissionsAndLoad() {
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                getApplication(),
                android.Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                getApplication(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
        
        if (hasPermission) {
            loadMusicFiles()
        } else {
            _errorMessage.value = "Permission required to access music files. Please grant permission in app settings."
        }
    }
    
    fun retryLoadMusic() {
        _errorMessage.value = null
        checkPermissionsAndLoad()
    }
    
    private fun loadMusicFiles() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                val tracks = musicScanner.scanMusicFiles()
                _trackCount.value = tracks.size
                
                if (tracks.isNotEmpty()) {
                    playerManager.setQueueAndPlay(tracks, 0)
                    playerManager.pause() // Start paused
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "No music files found. Please add music to your device and try again."
                }
            } catch (e: SecurityException) {
                _errorMessage.value = "Permission denied. Please grant storage permission in Settings."
            } catch (e: Exception) {
                _errorMessage.value = "Error loading music: ${e.message}\n\nTrying to find: MP3, FLAC, AAC files in Music/Downloads folders."
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
