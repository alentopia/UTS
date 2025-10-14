package com.example.testing.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import com.example.testing.R

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val firebaseAuth = FirebaseAuth.getInstance()

    // 🔄 Animasi muncul & navigasi
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(durationMillis = 600)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400)
        )

        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, delayMillis = 200)
        )

        delay(1600)

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("signin") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // 🌈 Tampilan splash
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 💜 Logo kamu di tengah (pakai gambar)
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .scale(scale.value)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.8f),
                                Color(0xFFEEDCFF).copy(alpha = 0.2f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo), // 🔹 ganti sesuai nama file logomu
                    contentDescription = "App Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(170.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ✨ Nama aplikasi & tagline
            Text(
                text = "SENTIO",
                fontSize = 40.sp,
                color = Color(0xFF8B4CFC),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(alpha.value),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Feel. Detect. Reflect.",
                fontSize = 16.sp,
                color = Color(0xFF6E6E6E),
                modifier = Modifier.alpha(alpha.value),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
