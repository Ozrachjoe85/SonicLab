package com.soniclab.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * SonicLab Application Class
 * 
 * Entry point for the app
 * Hilt is initialized here
 */
@HiltAndroidApp
class SonicLabApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // App initialization happens here
        // Hilt handles dependency injection automatically
        // PlayerManager and MusicScanner are singletons
    }
}
