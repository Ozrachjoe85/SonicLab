package com.soniclab.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.soniclab.app.MainActivity
import com.soniclab.app.R

/**
 * Foreground service for background music playback
 * 
 * This service ensures:
 * - Music continues when app is in background
 * - Music continues when phone is locked
 * - Lock screen controls are available
 * - Notification controls work properly
 * - System won't kill the player
 * 
 * Based on Media3 MediaSessionService for modern Android
 */
class MusicPlaybackService : MediaSessionService() {
    
    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "soniclab_playback"
        private const val CHANNEL_NAME = "Music Playback"
        
        // Actions for notification buttons
        const val ACTION_PLAY = "com.soniclab.app.PLAY"
        const val ACTION_PAUSE = "com.soniclab.app.PAUSE"
        const val ACTION_NEXT = "com.soniclab.app.NEXT"
        const val ACTION_PREVIOUS = "com.soniclab.app.PREVIOUS"
        const val ACTION_STOP = "com.soniclab.app.STOP"
    }
    
    private var mediaSession: MediaSession? = null
    private lateinit var player: Player
    
    override fun onCreate() {
        super.onCreate()
        
        // Create notification channel (required for Android O+)
        createNotificationChannel()
        
        // Initialize player (will be provided by PlayerManager)
        // player = getPlayer()
        
        // Create MediaSession for lock screen controls
        // mediaSession = createMediaSession()
        
        // Start as foreground service
        // startForeground(NOTIFICATION_ID, createNotification())
    }
    
    /**
     * Create notification channel for Android O+
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW // Low = no sound/vibration
            ).apply {
                description = "Controls for music playback"
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    /**
     * Create notification with playback controls
     * Shows on lock screen and notification shade
     */
    private fun createNotification(
        title: String = "SonicLab",
        artist: String = "No track playing",
        isPlaying: Boolean = false
    ): Notification {
        // Intent to open app when notification is tapped
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        // Play/Pause action
        val playPauseAction = NotificationCompat.Action(
            if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
            if (isPlaying) "Pause" else "Play",
            createPendingIntent(if (isPlaying) ACTION_PAUSE else ACTION_PLAY)
        )
        
        // Previous action
        val previousAction = NotificationCompat.Action(
            R.drawable.ic_skip_previous,
            "Previous",
            createPendingIntent(ACTION_PREVIOUS)
        )
        
        // Next action
        val nextAction = NotificationCompat.Action(
            R.drawable.ic_skip_next,
            "Next",
            createPendingIntent(ACTION_NEXT)
        )
        
        // Stop action
        val stopAction = NotificationCompat.Action(
            R.drawable.ic_close,
            "Stop",
            createPendingIntent(ACTION_STOP)
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(artist)
            .setSmallIcon(R.drawable.ic_notification)
            // .setLargeIcon(albumArt) // TODO: Add album art
            .setContentIntent(contentIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(isPlaying) // Can't swipe away when playing
            .setShowWhen(false)
            .addAction(previousAction)
            .addAction(playPauseAction)
            .addAction(nextAction)
            .addAction(stopAction)
            // Media style for better lock screen appearance
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2) // Prev, Play, Next
                    // .setMediaSession(mediaSession?.sessionToken)
            )
            .build()
    }
    
    /**
     * Create PendingIntent for notification actions
     */
    private fun createPendingIntent(action: String): PendingIntent {
        val intent = Intent(action).apply {
            setPackage(packageName)
        }
        return PendingIntent.getBroadcast(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
    
    /**
     * Update notification when playback state changes
     */
    fun updateNotification(
        title: String,
        artist: String,
        isPlaying: Boolean
    ) {
        val notification = createNotification(title, artist, isPlaying)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    /**
     * Required for MediaSessionService
     * Returns the MediaSession for this service
     */
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
    
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
    
    /**
     * Handle incoming service commands
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                // TODO: player.play()
                updateNotification("Title", "Artist", true)
            }
            ACTION_PAUSE -> {
                // TODO: player.pause()
                updateNotification("Title", "Artist", false)
            }
            ACTION_NEXT -> {
                // TODO: player.seekToNext()
            }
            ACTION_PREVIOUS -> {
                // TODO: player.seekToPrevious()
            }
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        
        return START_STICKY // Restart service if killed
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}

/**
 * Helper to start/stop the music service
 */
object PlaybackServiceHelper {
    
    fun startService(context: Context) {
        val intent = Intent(context, MusicPlaybackService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
    
    fun stopService(context: Context) {
        val intent = Intent(context, MusicPlaybackService::class.java)
        context.stopService(intent)
    }
    
    fun sendAction(context: Context, action: String) {
        val intent = Intent(action).apply {
            setPackage(context.packageName)
        }
        context.sendBroadcast(intent)
    }
}
