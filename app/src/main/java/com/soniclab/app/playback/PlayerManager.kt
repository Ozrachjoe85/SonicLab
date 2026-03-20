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
 */
@Singleton
class PlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private var player: ExoPlayer? = null
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()
    
    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()
    
    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    val queue: StateFlow<List<Track>> = _queue.asStateFlow()
    
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()
    
    fun initialize() {
        if (player == null) {
            player = ExoPlayer.Builder(context)
                .build()
                .apply {
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
                                    playNext()
                                }
                            }
                        }
                    })
                }
        }
    }
    
    fun setQueueAndPlay(tracks: List<Track>, startIndex: Int = 0) {
        if (tracks.isEmpty()) return
        
        _queue.value = tracks
        _currentIndex.value = startIndex
        _currentTrack.value = tracks[startIndex]
        
        val mediaItems = tracks.map { track ->
            MediaItem.fromUri(track.uri)
        }
        
        player?.apply {
            setMediaItems(mediaItems, startIndex, 0)
            prepare()
            play()
        }
    }
    
    fun play() {
        player?.play()
    }
    
    fun pause() {
        player?.pause()
    }
    
    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            play()
        }
    }
    
    fun playNext() {
        val nextIndex = _currentIndex.value + 1
        if (nextIndex < _queue.value.size) {
            _currentIndex.value = nextIndex
            _currentTrack.value = _queue.value[nextIndex]
            player?.seekToNext()
        }
    }
    
    fun playPrevious() {
        val prevIndex = _currentIndex.value - 1
        if (prevIndex >= 0) {
            _currentIndex.value = prevIndex
            _currentTrack.value = _queue.value[prevIndex]
            player?.seekToPrevious()
        } else {
            player?.seekTo(0)
        }
    }
    
    fun seekTo(positionMs: Long) {
        player?.seekTo(positionMs)
    }
    
    fun getCurrentPosition(): Long {
        return player?.currentPosition ?: 0L
    }
    
    fun getDuration(): Long {
        return player?.duration ?: 0L
    }
    
    fun release() {
        player?.release()
        player = null
    }
    
    fun getPlayer(): Player? = player
}

data class Track(
    val id: Long,
    val uri: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val albumArtUri: String? = null
)
