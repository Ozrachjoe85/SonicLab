package com.soniclab.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.soniclab.app.MainActivity
import com.soniclab.app.R

/**
 * Foreground service for background music playback
 */
class MusicPlaybackService : MediaSessionService() {
    
    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "soniclab_playback"
        private const val CHANNEL_NAME = "Music Playback"
        
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
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Controls for music playback"
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(
        title: String = "SonicLab",
        artist: String = "No track playing",
        isPlaying: Boolean = false
    ): Notification {
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val playPauseAction = NotificationCompat.Action(
            if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
            if (isPlaying) "Pause" else "Play",
            createPendingIntent(if (isPlaying) ACTION_PAUSE else ACTION_PLAY)
        )
        
        val previousAction = NotificationCompat.Action(
            R.drawable.ic_skip_previous,
            "Previous",
            createPendingIntent(ACTION_PREVIOUS)
        )
        
        val nextAction = NotificationCompat.Action(
            R.drawable.ic_skip_next,
            "Next",
            createPendingIntent(ACTION_NEXT)
        )
        
        val stopAction = NotificationCompat.Action(
            R.drawable.ic_close,
            "Stop",
            createPendingIntent(ACTION_STOP)
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(artist)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(contentIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(isPlaying)
            .setShowWhen(false)
            .addAction(previousAction)
            .addAction(playPauseAction)
            .addAction(nextAction)
            .addAction(stopAction)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .build()
    }
    
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
    
    fun updateNotification(title: String, artist: String, isPlaying: Boolean) {
        val notification = createNotification(title, artist, isPlaying)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
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
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> updateNotification("Title", "Artist", true)
            ACTION_PAUSE -> updateNotification("Title", "Artist", false)
            ACTION_NEXT -> { /* TODO */ }
            ACTION_PREVIOUS -> { /* TODO */ }
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}

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
