package com.example.testing.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MoodPickerScreen(
    navController: NavController? = null,
    onContinue: (String, String) -> Unit
) {
    val moods = listOf("Angry", "Sad", "Neutral", "Happy", "Loved")
    val emojis = listOf("😡", "😔", "😐", "😊", "😍")
    val colors = listOf(
        Color(0xFFFF9AA2), // Angry
        Color(0xFFA5D8FF), // Sad
        Color(0xFFCFC9FF), // Neutral (lavender gray)
        Color(0xFFB8F2E6), // Happy (mint)
        Color(0xFFFFB5E8)  // Loved
    )

    fun Float.toRad(): Float = (this * (PI / 180f)).toFloat()
    val sweep = 360f / moods.size
    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    var selectedIndex by remember { mutableStateOf(2) }
    var lastAngle by remember { mutableStateOf(0f) }
    var showCameraPopup by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Back
        IconButton(
            onClick = { navController?.navigate("journal_list") },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF8B4CFC))
        }

        // 🎯 Gesture rotasi
        val gestureModifier = Modifier.pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    lastAngle = atan2(
                        offset.y - size.height / 2,
                        offset.x - size.width / 2
                    ) * (180f / PI).toFloat()
                },
                onDrag = { change, _ ->
                    val currentAngle = atan2(
                        change.position.y - size.height / 2,
                        change.position.x - size.width / 2
                    ) * (180f / PI).toFloat()
                    val delta = currentAngle - lastAngle
                    lastAngle = currentAngle
                    coroutineScope.launch {
                        rotation.snapTo(rotation.value + delta)
                    }
                },
                onDragEnd = {
                    coroutineScope.launch {
                        val normalized = ((rotation.value % 360) + 360) % 360
                        val index = ((normalized + sweep / 2) / sweep).toInt() % moods.size
                        selectedIndex = (moods.size - index) % moods.size
                    }
                }
            )
        }

        // 🎡 Mood Wheel
        Canvas(
            modifier = Modifier
                .size(310.dp)
                .then(gestureModifier)
                .shadow(12.dp, CircleShape)
        ) {
            val radius = size.minDimension / 2.3f
            var startAngle = rotation.value - 90f

            moods.forEachIndexed { index, _ ->
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    style = Fill
                )

                val middle = startAngle + sweep / 2
                val rad = middle.toFloat().toRad()
                val x = (size.width / 2 + cos(rad) * (radius * 0.7f))
                val y = (size.height / 2 + sin(rad) * (radius * 0.7f))

                drawContext.canvas.nativeCanvas.drawText(
                    emojis[index],
                    x,
                    y,
                    android.graphics.Paint().apply {
                        textSize = 65f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }
                )
                startAngle += sweep
            }

        }

        //  Pointer
        Canvas(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center)
        ) {
            val cx = size.width / 2
            val ty = size.height / 2 - (size.minDimension / 2.15f) - 45f
            val path = Path().apply {
                moveTo(cx - 20f, ty)
                arcTo(Rect(cx - 20f, ty - 20f, cx + 20f, ty + 20f), 180f, 180f, false)
                lineTo(cx, ty + 50f)
                close()
            }
            drawPath(path, Color(0xFF8B4CFC))
        }

        // 😄 Teks mood
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
        ) {
            Text(text = emojis[selectedIndex], fontSize = 60.sp)
            Text(
                text = "You feel ${moods[selectedIndex]} today!",
                fontSize = 18.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
        }

        // 🔘 Tombol
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            // Mood
            Button(
                onClick = {
                    showCameraPopup = true
                    coroutineScope.launch {
                        delay(2000)
                        showCameraPopup = false
                        val detectedIndex = 3
                        selectedIndex = detectedIndex
                        val targetRotation = -detectedIndex * sweep
                        rotation.animateTo(
                            targetValue = targetRotation,
                            animationSpec = tween(900)
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(50.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF8B4CFC), Color(0xFFD58FFF))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) { Text("Scan Your Mood", color = Color.White, fontWeight = FontWeight.Bold) }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onContinue(emojis[selectedIndex], moods[selectedIndex]) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4CFC)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(50.dp)
            ) { Text("Continue ➜", color = Color.White) }
        }
    }
    CameraPopup(showCameraPopup = showCameraPopup)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CameraPopup(showCameraPopup: Boolean) {
    AnimatedVisibility(
        visible = showCameraPopup,
        enter = fadeIn(animationSpec = tween(250)) +
                scaleIn(initialScale = 0.8f, animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(200)) +
                scaleOut(targetScale = 0.8f, animationSpec = tween(200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(190.dp)
                    .shadow(12.dp, RoundedCornerShape(28.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Color(0xFF8B4CFC),
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Detecting your mood...",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF8B4CFC)
                    )
                }
            }
        }
    }
}