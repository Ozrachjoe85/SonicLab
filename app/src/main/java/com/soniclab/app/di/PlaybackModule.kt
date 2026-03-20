package com.soniclab.app.di

import android.content.Context
import com.soniclab.app.playback.MusicScanner
import com.soniclab.app.playback.PlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for playback-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object PlaybackModule {
    
    @Provides
    @Singleton
    fun providePlayerManager(
        @ApplicationContext context: Context
    ): PlayerManager {
        return PlayerManager(context)
    }
    
    @Provides
    @Singleton
    fun provideMusicScanner(
        @ApplicationContext context: Context
    ): MusicScanner {
        return MusicScanner(context)
    }
}
