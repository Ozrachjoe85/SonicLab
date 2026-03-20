package com.soniclab.app.playback

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * MusicScanner - Scans device for audio files
 */
@Singleton
class MusicScanner @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    suspend fun scanMusicFiles(): List<Track> = withContext(Dispatchers.IO) {
        val tracks = mutableListOf<Track>()
        
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )
        
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        
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
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn) ?: "Unknown Title"
                val artist = it.getString(artistColumn) ?: "Unknown Artist"
                val album = it.getString(albumColumn) ?: "Unknown Album"
                val duration = it.getLong(durationColumn)
                val albumId = it.getLong(albumIdColumn)
                
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                
                val albumArtUri = ContentUris.withAppendedId(
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
        
        tracks
    }
    
    suspend fun getTracksByArtist(artist: String): List<Track> = withContext(Dispatchers.IO) {
        scanMusicFiles().filter { it.artist == artist }
    }
    
    suspend fun getTracksByAlbum(album: String): List<Track> = withContext(Dispatchers.IO) {
        scanMusicFiles().filter { it.album == album }
    }
    
    suspend fun searchTracks(query: String): List<Track> = withContext(Dispatchers.IO) {
        scanMusicFiles().filter { 
            it.title.contains(query, ignoreCase = true) ||
            it.artist.contains(query, ignoreCase = true) ||
            it.album.contains(query, ignoreCase = true)
        }
    }
    
    suspend fun getAllArtists(): List<String> = withContext(Dispatchers.IO) {
        scanMusicFiles()
            .map { it.artist }
            .distinct()
            .sorted()
    }
    
    suspend fun getAllAlbums(): List<String> = withContext(Dispatchers.IO) {
        scanMusicFiles()
            .map { it.album }
            .distinct()
            .sorted()
    }
}
