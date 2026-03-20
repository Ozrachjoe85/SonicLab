package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.soniclab.app.playback.MusicScanner
import com.soniclab.app.playback.PlayerManager
import com.soniclab.app.playback.Track
import com.soniclab.app.ui.theme.sonicColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * CollectionViewModel - Manages library data
 */
@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val musicScanner: MusicScanner,
    private val playerManager: PlayerManager
) : ViewModel() {
    
    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks.asStateFlow()
    
    private val _artists = MutableStateFlow<List<String>>(emptyList())
    val artists: StateFlow<List<String>> = _artists.asStateFlow()
    
    private val _albums = MutableStateFlow<List<String>>(emptyList())
    val albums: StateFlow<List<String>> = _albums.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadLibrary()
    }
    
    private fun loadLibrary() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _tracks.value = musicScanner.scanMusicFiles()
                _artists.value = musicScanner.getAllArtists()
                _albums.value = musicScanner.getAllAlbums()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun playTrack(track: Track) {
        val trackList = _tracks.value
        val index = trackList.indexOf(track)
        if (index >= 0) {
            playerManager.setQueueAndPlay(trackList, index)
        }
    }
    
    fun playArtist(artist: String) {
        viewModelScope.launch {
            val artistTracks = musicScanner.getTracksByArtist(artist)
            if (artistTracks.isNotEmpty()) {
                playerManager.setQueueAndPlay(artistTracks, 0)
            }
        }
    }
    
    fun playAlbum(album: String) {
        viewModelScope.launch {
            val albumTracks = musicScanner.getTracksByAlbum(album)
            if (albumTracks.isNotEmpty()) {
                playerManager.setQueueAndPlay(albumTracks, 0)
            }
        }
    }
}

/**
 * CollectionScreen - Browse music library
 */
@Composable
fun CollectionScreen(
    modifier: Modifier = Modifier,
    viewModel: CollectionViewModel = hiltViewModel()
) {
    val colors = sonicColors
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Songs", "Artists", "Albums")
    
    val tracks by viewModel.tracks.collectAsState()
    val artists by viewModel.artists.collectAsState()
    val albums by viewModel.albums.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = colors.surface,
            contentColor = colors.primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            title,
                            color = if (selectedTab == index) colors.primary else colors.textSecondary,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
        
        // Content
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.primary)
            }
        } else {
            when (selectedTab) {
                0 -> SongsTab(tracks, viewModel)
                1 -> ArtistsTab(artists, viewModel)
                2 -> AlbumsTab(albums, viewModel)
            }
        }
    }
}

@Composable
private fun SongsTab(tracks: List<Track>, viewModel: CollectionViewModel) {
    val colors = sonicColors
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tracks) { track ->
            TrackItem(track = track, onClick = { viewModel.playTrack(track) })
        }
    }
}

@Composable
private fun ArtistsTab(artists: List<String>, viewModel: CollectionViewModel) {
    val colors = sonicColors
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(artists) { artist ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.playArtist(artist) },
                colors = CardDefaults.cardColors(
                    containerColor = colors.surface
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = colors.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = artist,
                        color = colors.textPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AlbumsTab(albums: List<String>, viewModel: CollectionViewModel) {
    val colors = sonicColors
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(albums) { album ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.playAlbum(album) },
                colors = CardDefaults.cardColors(
                    containerColor = colors.surface
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Album,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = colors.secondary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = album,
                        color = colors.textPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun TrackItem(track: Track, onClick: () -> Unit) {
    val colors = sonicColors
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album art thumbnail
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.background)
            ) {
                if (track.albumArtUri != null) {
                    AsyncImage(
                        model = track.albumArtUri,
                        contentDescription = "Album art",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.MusicNote,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp),
                        tint = colors.textSecondary.copy(alpha = 0.3f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Track info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${track.artist} • ${track.album}",
                    color = colors.textSecondary,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Duration
            Text(
                text = formatDuration(track.duration),
                color = colors.textTertiary,
                fontSize = 12.sp
            )
        }
    }
}

private fun formatDuration(ms: Long): String {
    val seconds = (ms / 1000).toInt()
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}
