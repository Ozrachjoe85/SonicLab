package com.soniclab.app.service

import android.app.*
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.util.Base64
import androidx.core.app.NotificationCompat
import com.soniclab.app.MainActivity
import com.soniclab.app.R
import com.soniclab.app.playback.PlayerManager
import com.soniclab.app.playback.Track
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlaybackService : Service() {
    
    @Inject lateinit var playerManager: PlayerManager
    
    private val binder = MusicBinder()
    private var notificationManager: NotificationManager? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    inner class MusicBinder : Binder() {
        fun getService(): MusicPlaybackService = this@MusicPlaybackService
    }
    
    companion object {
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "soniclab_playback"
        const val ACTION_PLAY = "com.soniclab.PLAY"
        const val ACTION_PAUSE = "com.soniclab.PAUSE"
        const val ACTION_NEXT = "com.soniclab.NEXT"
        const val ACTION_PREVIOUS = "com.soniclab.PREVIOUS"
    }
    
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            when (i?.action) {
                ACTION_PLAY -> playerManager.play()
                ACTION_PAUSE -> playerManager.pause()
                ACTION_NEXT -> playerManager.playNext()
                ACTION_PREVIOUS -> playerManager.playPrevious()
            }
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NotificationManager::class.java)
        createNotificationChannel()
        
        val filter = IntentFilter().apply {
            addAction(ACTION_PLAY); addAction(ACTION_PAUSE)
            addAction(ACTION_NEXT); addAction(ACTION_PREVIOUS)
        }
        if (Build.VERSION.SDK_INT >= 33) registerReceiver(receiver, filter, RECEIVER_EXPORTED)
        else registerReceiver(receiver, filter)
        
        startForeground(NOTIFICATION_ID, createNotification())
        playerManager.isPlaying.onEach { updateNotification() }.launchIn(serviceScope)
        playerManager.currentTrack.onEach { updateNotification() }.launchIn(serviceScope)
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            notificationManager?.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "Playback", NotificationManager.IMPORTANCE_LOW).apply {
                    setShowBadge(false)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
            )
        }
    }
    
    fun updateNotification() {
        notificationManager?.notify(NOTIFICATION_ID, createNotification())
    }
    
    private fun createNotification(): Notification {
        val track = playerManager.currentTrack.value
        val playing = playerManager.isPlaying.value
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(track?.title ?: "SonicLab")
            .setContentText(track?.artist ?: "Ready")
            .setSubText(track?.album)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(loadArt(track))
            .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(playing)
            .setShowWhen(false)
            .addAction(action(R.drawable.ic_skip_previous, "Prev", ACTION_PREVIOUS))
            .addAction(if (playing) action(R.drawable.ic_pause, "Pause", ACTION_PAUSE) else action(R.drawable.ic_play, "Play", ACTION_PLAY))
            .addAction(action(R.drawable.ic_skip_next, "Next", ACTION_NEXT))
            .build()
    }
    
    private fun loadArt(t: Track?): Bitmap? = t?.albumArtUri?.let { uri ->
        try {
            if (uri.startsWith("data:")) {
                val b64 = uri.substringAfter("base64,")
                val bytes = Base64.decode(b64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } else contentResolver.openInputStream(android.net.Uri.parse(uri))?.use { BitmapFactory.decodeStream(it) }
        } catch (e: Exception) { null }
    }
    
    private fun action(icon: Int, title: String, a: String) = NotificationCompat.Action(
        icon, title, PendingIntent.getBroadcast(this, a.hashCode(), Intent(a).setPackage(packageName), PendingIntent.FLAG_IMMUTABLE)
    )
    
    override fun onBind(intent: Intent): IBinder = binder
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY
    override fun onDestroy() {
        serviceScope.cancel()
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}
