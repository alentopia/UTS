package com.example.spotifydemo.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.spotifydemo.data.SpotifyRepository
import kotlinx.coroutines.launch

@Composable
fun MusicScreen(
    clientId: String,
    clientSecret: String
) {
    val repo = remember { SpotifyRepository(clientId, clientSecret) }
    var playlist by remember { mutableStateOf<com.example.spotifydemo.data.PlaylistResponse?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Fetch playlist
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                playlist = repo.getMyPlaylist()
            } catch (e: Exception) {
                e.printStackTrace()
                error = "Cannot open the music player 😢"
            } finally {
                loading = false
            }
        }
    }

    // Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFEED3F2),
                        Color(0xFFD1E5FF)
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        when {
            loading -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF8B4CFC))
                }
            }

            error != null -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(
                        text = error ?: "",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            playlist != null -> {
                val track = playlist!!.tracks.items.firstOrNull()?.track

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //  MINI MUSIC PREVIEW
                    if (track != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 10.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.5f))
                                .padding(20.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(track.album.images.firstOrNull()?.url),
                                contentDescription = track.name,
                                modifier = Modifier
                                    .size(220.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable {
                                        val url = "https://open.spotify.com/search/${track.name}"
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                    }
                            )

                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = track.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color(0xFF2A2A2A),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = track.artists.joinToString { it.name },
                                fontSize = 14.sp,
                                color = Color(0xFF8B4CFC),
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(16.dp))
                            TextButton(onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://open.spotify.com/playlist/04vFVSHKvDSOVC16f2QDtH")
                                )
                                context.startActivity(intent)
                            }) {
                                Text(
                                    text = "Open in Spotify",
                                    color = Color(0xFF8B4CFC),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    //  Judul playlist di bawah preview
                    Text(
                        text = playlist!!.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = Color(0xFF2A2A2A),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Divider(
                        color = Color(0xFFDDCFF9),
                        thickness = 1.dp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    //  Daftar lagu
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(playlist!!.tracks.items.drop(1)) { item ->
                            val trackItem = item.track
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.8f))
                                    .clickable {
                                        val url =
                                            "https://open.spotify.com/search/${trackItem.name} ${trackItem.artists.firstOrNull()?.name ?: ""}"
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                    }
                                    .padding(10.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(trackItem.album.images.firstOrNull()?.url),
                                    contentDescription = trackItem.name,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Spacer(Modifier.width(12.dp))

                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = trackItem.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color(0xFF222222)
                                    )
                                    Text(
                                        text = trackItem.artists.joinToString { it.name },
                                        fontSize = 13.sp,
                                        color = Color(0xFF8B4CFC)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
