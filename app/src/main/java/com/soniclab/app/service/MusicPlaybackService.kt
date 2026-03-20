package com.soniclab.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Base64
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.soniclab.app.MainActivity
import com.soniclab.app.R
import com.soniclab.app.playback.PlayerManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlaybackService : MediaSessionService() {
    
    @Inject
    lateinit var playerManager: PlayerManager
    
    private var mediaSession: MediaSession? = null
    private var notificationManager: NotificationManager? = null
    
    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "soniclab_playback"
        const val ACTION_PLAY = "com.soniclab.PLAY"
        const val ACTION_PAUSE = "com.soniclab.PAUSE"
        const val ACTION_NEXT = "com.soniclab.NEXT"
        const val ACTION_PREVIOUS = "com.soniclab.PREVIOUS"
        const val ACTION_STOP = "com.soniclab.STOP"
    }
    
    private val actionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                ACTION_PLAY -> playerManager.play()
                ACTION_PAUSE -> playerManager.pause()
                ACTION_NEXT -> playerManager.playNext()
                ACTION_PREVIOUS -> playerManager.playPrevious()
                ACTION_STOP -> {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                }
            }
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        
        notificationManager = getSystemService(NotificationManager::class.java)
        createNotificationChannel()
        
        // Register broadcast receiver
        val filter = IntentFilter().apply {
            addAction(ACTION_PLAY)
            addAction(ACTION_PAUSE)
            addAction(ACTION_NEXT)
            addAction(ACTION_PREVIOUS)
            addAction(ACTION_STOP)
        }
        registerReceiver(actionReceiver, filter, RECEIVER_EXPORTED)
        
        // Initialize MediaSession
        val player = playerManager.getPlayer()
        mediaSession = MediaSession.Builder(this, player).build()
        
        // Start foreground with initial notification
        startForeground(NOTIFICATION_ID, createNotification())
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Controls for music playback"
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
    
    fun updateNotification() {
        val notification = createNotification()
        notificationManager?.notify(NOTIFICATION_ID, notification)
    }
    
    private fun createNotification(): Notification {
        val track = playerManager.currentTrack.value
        val isPlaying = playerManager.isPlaying.value
        
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        // Album art
        val albumArt = track?.albumArtUri?.let { uri ->
            if (uri.startsWith("data:image")) {
                // Base64 embedded art
                val base64 = uri.substringAfter("base64,")
                val bytes = Base64.decode(base64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } else {
                // MediaStore URI
                try {
                    contentResolver.openInputStream(android.net.Uri.parse(uri))?.use {
                        BitmapFactory.decodeStream(it)
                    }
                } catch (e: Exception) {
                    null
                }
            }
        }
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(track?.title ?: "SonicLab")
            .setContentText(track?.artist ?: "No track playing")
            .setSubText(track?.album)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(albumArt)
            .setContentIntent(contentIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(isPlaying)
            .setShowWhen(false)
            .addAction(createAction(R.drawable.ic_skip_previous, "Previous", ACTION_PREVIOUS))
            .addAction(
                if (isPlaying) createAction(R.drawable.ic_pause, "Pause", ACTION_PAUSE)
                else createAction(R.drawable.ic_play, "Play", ACTION_PLAY)
            )
            .addAction(createAction(R.drawable.ic_skip_next, "Next", ACTION_NEXT))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(MediaSessionCompat.Token.fromToken(mediaSession?.sessionCompatToken)))
            .build()
    }
    
    private fun createAction(icon: Int, title: String, action: String): NotificationCompat.Action {
        val intent = Intent(action).setPackage(packageName)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Action(icon, title, pendingIntent)
    }
    
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession
    
    override fun onDestroy() {
        unregisterReceiver(actionReceiver)
        mediaSession?.run {
            player.release()
            release()
        }
        super.onDestroy()
    }
    
    override fun onBind(intent: Intent?): IBinder? = super.onBind(intent)
}
