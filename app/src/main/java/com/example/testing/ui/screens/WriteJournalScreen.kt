package com.example.testing.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume

@Composable
fun WriteJournalScreen(
    emoji: String,
    mood: String,
    date: String,
    onSave: (String, String) -> Unit,
    onSkip: (String) -> Unit,
    navController: NavController? = null
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val coroutineScope = rememberCoroutineScope()

    var journalText by remember { mutableStateOf("") }
    var manualLocation by remember { mutableStateOf("") }
    var isFetchingLocation by remember { mutableStateOf(false) }
    var showVoiceDialog by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }

    val currentDate = remember {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
    }

    // 🔒 Izin lokasi
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) manualLocation = "Location permission not granted"
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // 🔝 Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController?.navigate("mood_picker") {
                    popUpTo("mood_picker") { inclusive = true }
                }
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }

            IconButton(onClick = {
                navController?.navigate("journal_list") {
                    popUpTo("journal_list") { inclusive = true }
                }
            }) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
            }
        }

        // 💜 Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = emoji, fontSize = 48.sp)
            Text(
                text = "I feel $mood",
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(date, color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(20.dp))

            // 📝 Text area + 🎙️ mic di kanan bawah
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
            ) {
                OutlinedTextField(
                    value = journalText,
                    onValueChange = { journalText = it },
                    placeholder = { Text("Write your thoughts here...") },
                    textStyle = TextStyle(fontSize = 16.sp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .matchParentSize()
                        .padding(end = 60.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                )

                // 🎙️ Voice note di pojok kanan bawah
                IconButton(
                    onClick = { showVoiceDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .size(46.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFBBA8FF), Color(0xFFB5C7FF))
                            ),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Record voice note",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 📍 Location input
            OutlinedTextField(
                value = manualLocation,
                onValueChange = { manualLocation = it },
                placeholder = { Text("Enter Location") },
                singleLine = true,
                textStyle = TextStyle(fontSize = 14.sp),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            val permissionCheck = ContextCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION
                            )
                            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                coroutineScope.launch {
                                    isFetchingLocation = true
                                    getCurrentLocation(context, fusedLocationClient) {
                                        manualLocation = it
                                    }
                                    isFetchingLocation = false
                                }
                            } else {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    ) {
                        if (isFetchingLocation) {
                            CircularProgressIndicator(
                                color = Color(0xFF8B4CFC),
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Detect location",
                                tint = Color(0xFF8B4CFC)
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF8B4CFC),
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color(0xFF8B4CFC)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 💾 Save Button
            Button(
                onClick = {
                    val finalLocation =
                        if (manualLocation.isNotBlank()) manualLocation else "Unknown location"
                    onSave("$journalText\n📍 Location: $finalLocation", currentDate)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4CFC)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Text("Save", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Skip and Save",
                color = Color(0xFF8B4CFC),
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSkip(currentDate) }
            )
        }
    }

    // 🎧 Voice Dialog (dipindah ke luar supaya overlay full-screen)
    if (showVoiceDialog) {
        VoiceDialog(
            isRecording = isRecording,
            onStartRecording = {
                isRecording = true
                coroutineScope.launch {
                    delay(3000)
                    isRecording = false
                    showVoiceDialog = false
                    journalText += "\n(🎙️ Voice note) “I had a busy day but I’m grateful.”"
                }
            },
            onCancel = { showVoiceDialog = false }
        )
    }
}

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(
    context: android.content.Context,
    fusedClient: com.google.android.gms.location.FusedLocationProviderClient,
    onLocationFound: (String) -> Unit
) {
    suspendCancellableCoroutine<Unit> { cont ->
        fusedClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val address = addresses?.firstOrNull()?.getAddressLine(0)
                    onLocationFound(address ?: "Lat: ${location.latitude}, Lng: ${location.longitude}")
                } else {
                    onLocationFound("Unable to get location")
                }
                cont.resume(Unit)
            }
            .addOnFailureListener {
                onLocationFound("Error getting location")
                cont.resume(Unit)
            }
    }
}

@Composable
fun VoiceDialog(
    isRecording: Boolean,
    onStartRecording: () -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            modifier = Modifier
                .wrapContentSize()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(min = 220.dp)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isRecording) {
                    AnimatedWaveform()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Listening...",
                        color = Color(0xFF8B4CFC),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        "Tap to start recording",
                        color = Color.DarkGray,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(
                            if (isRecording) Color(0xFF8B4CFC)
                            else Color(0xFFE7DBFF)
                        )
                        .clickable { if (!isRecording) onStartRecording() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Mic",
                        tint = if (isRecording) Color.White else Color(0xFF8B4CFC),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Cancel",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onCancel() }
                )
            }
        }
    }
}

@Composable
fun AnimatedWaveform() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waveHeights = List(10) { index ->
        infiniteTransition.animateFloat(
            initialValue = 5f,
            targetValue = 50f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 400 + index * 50, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = ""
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp)
    ) {
        val barWidth = size.width / (waveHeights.size * 2)
        waveHeights.forEachIndexed { index, anim ->
            val x = index * 2 * barWidth + barWidth / 2
            drawRoundRect(
                color = Color(0xFF8B4CFC),
                topLeft = androidx.compose.ui.geometry.Offset(x, size.height / 2 - anim.value / 2),
                size = androidx.compose.ui.geometry.Size(barWidth, anim.value),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
            )
        }
    }
}
