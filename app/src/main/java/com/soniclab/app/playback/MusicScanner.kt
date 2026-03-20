package com.soniclab.app.playback

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Enhanced MusicScanner - Filters zero-length tracks, extracts embedded album art
 */
@Singleton
class MusicScanner @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    suspend fun scanMusicFiles(): List<Track> = withContext(Dispatchers.IO) {
        val tracks = mutableListOf<Track>()
        
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )
        
        // CRITICAL: Filter out zero-length tracks
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DURATION} > 0"
        val sortOrder = "${MediaStore.Audio.Media.ARTIST} ASC, ${MediaStore.Audio.Media.ALBUM} ASC"
        
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )
        
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val pathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val filePath = it.getString(pathColumn)
                val title = it.getString(titleColumn) ?: "Unknown Title"
                val artist = it.getString(artistColumn) ?: "Unknown Artist"
                val album = it.getString(albumColumn) ?: "Unknown Album"
                val duration = it.getLong(durationColumn)
                val albumId = it.getLong(albumIdColumn)
                
                if (duration <= 0) continue
                
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                
                // Extract embedded album art
                val albumArtUri = extractEmbeddedAlbumArt(filePath) ?: ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                ).toString()
                
                tracks.add(
                    Track(
                        id = id,
                        uri = contentUri.toString(),
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        albumArtUri = albumArtUri
                    )
                )
            }
        }
        
        Log.d("MusicScanner", "Found ${tracks.size} valid tracks")
        tracks
    }
    
    private fun extractEmbeddedAlbumArt(filePath: String): String? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(filePath)
            val artBytes = retriever.embeddedPicture
            retriever.release()
            
            if (artBytes != null) {
                val base64 = Base64.encodeToString(artBytes, Base64.NO_WRAP)
                "data:image/jpeg;base64,$base64"
            } else null
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getTracksByArtist(artist: String) = withContext(Dispatchers.IO) {
        scanMusicFiles().filter { it.artist == artist }
    }
    
    suspend fun getTracksByAlbum(album: String) = withContext(Dispatchers.IO) {
        scanMusicFiles().filter { it.album == album }
    }
    
    suspend fun searchTracks(query: String) = withContext(Dispatchers.IO) {
        scanMusicFiles().filter { 
            it.title.contains(query, true) ||
            it.artist.contains(query, true) ||
            it.album.contains(query, true)
        }
    }
    
    suspend fun getAllArtists() = withContext(Dispatchers.IO) {
        scanMusicFiles().map { it.artist }.distinct().sorted()
    }
    
    suspend fun getAllAlbums() = withContext(Dispatchers.IO) {
        scanMusicFiles().map { it.album }.distinct().sorted()
    }
}
