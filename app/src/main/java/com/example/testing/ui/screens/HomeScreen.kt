package com.example.testing.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController? = null) {
    val pastelBg = Brush.verticalGradient(
        colors = listOf(Color(0xFFEED3F2), Color(0xFFD1E5FF))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pastelBg)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // 👋 Greeting
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Hey, Valen!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF44345C)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Here’s your emotional journey 💜",
                    fontSize = 15.sp,
                    color = Color(0xFF7D7A8B)
                )
            }

            // 📊 3 kotak statistik
            StatsSection()

            // 🌿 Mood Stability card
            MoodStabilityCard()

            // 💭 Quote
            Text(
                "“Small steps every day still move you forward.”",
                color = Color(0xFF5E5E5E),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            // 🌸 Button
            Button(
                onClick = { navController?.navigate("mood_picker") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4CFC)),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    "Record New Mood",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            icon = "⚡",
            value = "65",
            label = "Longest streak",
            bgColor = Color(0xFFFFFFFF),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = "🔥",
            value = "58",
            label = "Current streak",
            bgColor = Color(0xFFFFFFFF),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = "📅",
            value = "482",
            label = "Lifetime days",
            bgColor = Color(0xFFFFFFFF),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    icon: String,
    value: String,
    label: String,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF44345C)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF5E5E5E)
            )
        }
    }
}

// 🌿 Mood Stability Card
@Composable
fun MoodStabilityCard() {
    val targetValue = 64
    val animatedValue = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedValue.animateTo(
            targetValue.toFloat(),
            animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFF7EFFF),
                            Color(0xFFFDFBFF)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // 🔮 Animated circular progress
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(90.dp)) {
                    Canvas(modifier = Modifier.size(90.dp)) {
                        val sweepAngle = (animatedValue.value / 100f) * 360f

                        drawArc(
                            color = Color(0xFFE2D2FF),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 14f, cap = StrokeCap.Round)
                        )

                        drawArc(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFFB37CFF),
                                    Color(0xFF8B4CFC)
                                )
                            ),
                            startAngle = -90f,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = 14f, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = animatedValue.value.toInt().toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF8B4CFC)
                        )
                        Text("/ 100", fontSize = 13.sp, color = Color(0xFF7D7A8B))
                    }
                }

                // ✨ Ubah teks biar terasa lebih "menyatu"
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4B3A64)
                        )
                        ) {
                            append("Mood Stability ")
                        }
                        withStyle(style = SpanStyle(color = Color(0xFF6D5A85))) {
                            append("— The higher the score, the more stable you are.")
                        }
                    },
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
