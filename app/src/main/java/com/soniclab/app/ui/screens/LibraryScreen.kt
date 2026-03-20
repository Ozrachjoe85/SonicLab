package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soniclab.app.ui.components.*
import com.soniclab.app.ui.theme.*

enum class LibraryTab {
    SONGS, ALBUMS, ARTISTS, PLAYLISTS
}

data class Song(
    val title: String,
    val artist: String,
    val album: String,
    val duration: String,
    val mood: String
)

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier
) {
    val themeManager = LocalThemeManager.current
    val colors = themeManager.colors
    
    var selectedTab by remember { mutableStateOf(LibraryTab.SONGS) }
    var searchQuery by remember { mutableStateOf("") }
    
    // Sample data (will be real music library in production)
    val songs = remember {
        listOf(
            Song("Cosmic Frequency", "SonicLab", "Digital Dreams", "3:42", "Energetic"),
            Song("Neon Pulse", "SynthWave", "Retro Future", "4:15", "Upbeat"),
            Song("Analog Warmth", "Vintage Sounds", "Tube Amps", "5:23", "Relaxing"),
            Song("Digital Ghost", "Cyber Souls", "Matrix Echoes", "3:58", "Mysterious"),
            Song("Plasma Dreams", "Electric Visions", "Neon Nights", "4:32", "Dreamy"),
            Song("Quantum Beat", "Bass Lab", "Deep Space", "3:17", "Energetic"),
            Song("Holographic Love", "Future Pop", "2084", "3:55", "Romantic"),
            Song("Circuit Breaker", "Tech Noir", "Industrial", "4:08", "Intense")
        )
    }
    
    val filteredSongs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            songs
        } else {
            songs.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.artist.contains(searchQuery, ignoreCase = true) ||
                it.album.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    CosmicVoidPanel(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ============================================
            // HEADER
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "◢ LIBRARY ◣",
                            color = NeonCyan,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        Text(
                            text = "${songs.size} TRACKS INDEXED",
                            color = PlasmaBlue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                    
                    NeonLED(
                        isOn = true,
                        color = NeonPurple,
                        label = "DB"
                    )
                }
            }
            
            // ============================================
            // SEARCH BAR
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "◆ Search tracks, artists, albums...",
                            color = TextSecondary.copy(alpha = 0.5f),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = NeonCyan
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = TextSecondary,
                                modifier = Modifier.clickable { searchQuery = "" }
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = NeonCyan,
                        unfocusedIndicatorColor = GridCyan.copy(alpha = 0.3f)
                    ),
                    singleLine = true
                )
            }
            
            // ============================================
            // TAB SELECTOR
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LibraryTab.values().forEach { tab ->
                        TabButton(
                            label = tab.name,
                            isSelected = selectedTab == tab,
                            onClick = { selectedTab = tab }
                        )
                    }
                }
            }
            
            // ============================================
            // AI MOOD CATEGORIES (Quick Filter)
            // ============================================
            DigitalGridPanel(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "⚡ AI MOOD FILTER",
                        color = PlasmaMagenta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Energetic", "Relaxing", "Upbeat", "Dreamy", "Intense").forEach { mood ->
                            MoodChip(
                                label = mood,
                                onClick = { searchQuery = mood }
                            )
                        }
                    }
                }
            }
            
            // ============================================
            // CONTENT LIST
            // ============================================
            DigitalGridPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (selectedTab) {
                                LibraryTab.SONGS -> "◆ ALL SONGS"
                                LibraryTab.ALBUMS -> "◆ ALBUMS"
                                LibraryTab.ARTISTS -> "◆ ARTISTS"
                                LibraryTab.PLAYLISTS -> "◆ PLAYLISTS"
                            },
                            color = GridCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        
                        Text(
                            text = "${filteredSongs.size} found",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    when (selectedTab) {
                        LibraryTab.SONGS -> SongsList(filteredSongs)
                        LibraryTab.ALBUMS -> AlbumsGrid()
                        LibraryTab.ARTISTS -> ArtistsGrid()
                        LibraryTab.PLAYLISTS -> PlaylistsGrid()
                    }
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) {
                    Brush.horizontalGradient(listOf(PlasmaViolet, PlasmaCyan))
                } else {
                    Brush.horizontalGradient(listOf(Color(0xFF222222), Color(0xFF111111)))
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) TextPrimary else Gray300,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun MoodChip(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(28.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        VUCosmicLow.copy(alpha = 0.3f),
                        VUCosmicMid.copy(alpha = 0.3f)
                    )
                )
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = NeonCyan,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun SongsList(songs: List<Song>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(songs) { song ->
            SongItem(song)
        }
    }
}

@Composable
private fun SongItem(song: Song) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF1A1A1A))
            .clickable { /* Play song */ }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album art placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        brush = Brush.radialGradient(
                            listOf(PlasmaViolet, PlasmaMagenta)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = NeonCyan.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = song.title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${song.artist} • ${song.album}",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "♪ ${song.mood}",
                    color = NeonCyan.copy(alpha = 0.7f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = song.duration,
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AlbumsGrid() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "◆ ALBUMS VIEW ◆\n(Coming Soon)",
            color = TextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun ArtistsGrid() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "◆ ARTISTS VIEW ◆\n(Coming Soon)",
            color = TextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun PlaylistsGrid() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "◆ PLAYLISTS VIEW ◆\n(Coming Soon)",
            color = TextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
        }
    }
}
