```
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep all classes in our package
-keep class com.soniclab.app.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep ExoPlayer classes
-keep class androidx.media3.** { *; }

# Keep Compose classes
-keep class androidx.compose.** { *; }
