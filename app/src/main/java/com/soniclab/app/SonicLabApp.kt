package com.soniclab.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * SonicLab Application Class
 * 
 * This is the entry point of our app. The @HiltAndroidApp annotation
 * initializes Hilt's dependency injection system.
 */
@HiltAndroidApp
class SonicLabApp : Application()
