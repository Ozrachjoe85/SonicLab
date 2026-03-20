package com.soniclab.app.playback

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * PlayerManager - Central controller for all audio playback
 * 
 * Manages:
 * - ExoPlayer instance
 * - Playback state (playing/paused/stopped)
 * - Current track info
 * - Queue management
 * - Progress tracking
 */
@Singleton
class PlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    // ExoPlayer instance
    private var player: ExoPlayer? = null
    
    // Playback state
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()
    
    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()
    
    // Queue
    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    val queue: StateFlow<List<Track>> = _queue.asStateFlow()
    
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()
    
    /**
     * Initialize the player
     */
    fun initialize() {
        if (player == null) {
            player = ExoPlayer.Builder(context)
                .build()
                .apply {
                    // Listen for playback state changes
                    addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            _isPlaying.value = isPlaying
                        }
                        
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            when (playbackState) {
                                Player.STATE_READY -> {
                                    _duration.value = duration
                                }
                                Player.STATE_ENDED -> {
                                    // Auto-play next track
                                    playNext()
                                }
                            }
                        }
                    })
                }
        }
    }
    
    /**
     * Set the queue and start playing
     */
    fun setQueueAndPlay(tracks: List<Track>, startIndex: Int = 0) {
        if (tracks.isEmpty()) return
        
        _queue.value = tracks
        _currentIndex.value = startIndex
        _currentTrack.value = tracks[startIndex]
        
        // Convert tracks to MediaItems
        val mediaItems = tracks.map { track ->
            MediaItem.fromUri(track.uri)
        }
        
        player?.apply {
            setMediaItems(mediaItems, startIndex, 0)
            prepare()
            play()
        }
    }
    
    /**
     * Play/Resume
     */
    fun play() {
        player?.play()
    }
    
    /**
     * Pause
     */
    fun pause() {
        player?.pause()
    }
    
    /**
     * Toggle play/pause
     */
    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            play()
        }
    }
    
    /**
     * Play next track in queue
     */
    fun playNext() {
        val nextIndex = _currentIndex.value + 1
        if (nextIndex < _queue.value.size) {
            _currentIndex.value = nextIndex
            _currentTrack.value = _queue.value[nextIndex]
            player?.seekToNext()
        }
    }
    
    /**
     * Play previous track in queue
     */
    fun playPrevious() {
        val prevIndex = _currentIndex.value - 1
        if (prevIndex >= 0) {
            _currentIndex.value = prevIndex
            _currentTrack.value = _queue.value[prevIndex]
            player?.seekToPrevious()
        } else {
            // If at first track, restart it
            player?.seekTo(0)
        }
    }
    
    /**
     * Seek to position (in milliseconds)
     */
    fun seekTo(positionMs: Long) {
        player?.seekTo(positionMs)
    }
    
    /**
     * Get current playback position
     */
    fun getCurrentPosition(): Long {
        return player?.currentPosition ?: 0L
    }
    
    /**
     * Get duration of current track
     */
    fun getDuration(): Long {
        return player?.duration ?: 0L
    }
    
    /**
     * Release player resources
     */
    fun release() {
        player?.release()
        player = null
    }
    
    /**
     * Get the underlying ExoPlayer instance
     * (for service integration)
     */
    fun getPlayer(): Player? = player
}

/**
 * Track data class
 * Represents a single music track
 */
data class Track(
    val id: Long,
    val uri: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val albumArtUri: String? = null
)
