package com.example.testing.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalDetailScreen(
    title: String,
    content: String,
    date: String,
    location: String,
    emoji: String,
    navController: NavController,
    edited: Boolean = false
) {
    val scrollState = rememberScrollState()
    val quotes = listOf(
        "“Every emotion is valid — and every day is progress.”",
        "“You’re allowed to feel. You’re allowed to rest.”",
        "“Healing is not linear, and that’s okay.”",
        "“You did your best today — and that’s enough.”",
        "“Small steps still move you forward.”",
        "“Don’t rush the process. Growth takes time.”",
        "“Even on cloudy days, the sun is still there.”",
        "“Your feelings are temporary, but your strength is lasting.”"
    )
    val randomQuote = remember { quotes[Random.nextInt(quotes.size)] }

    // Snackbar setup
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Muncul otomatis kalau edited = true
    LaunchedEffect(edited) {
        if (edited) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Journal has been edited")
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Tombol atas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.navigate("journal_list") {
                    popUpTo("journal_list") { inclusive = true }
                }
            }) {

            Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF8B4CFC)
                )
            }

            IconButton(
                onClick = {
                    // Navigasi ke edit screen
                    navController.navigate(
                        "edit_journal/${title}/${content}/${date}/${emoji}/${location}"
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Journal",
                    tint = Color(0xFF8B4CFC)
                )
            }
        }

        // Isi konten jurnal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 56.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 70.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF44345C)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$location • $date",
                fontSize = 13.sp,
                color = Color(0xFF7D7A8B)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Box isi jurnal
            Surface(
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 6.dp,
                color = Color.White.copy(alpha = 0.95f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = content.ifBlank { "No journal content written." },
                        fontSize = 16.sp,
                        color = Color(0xFF444444),
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Random quote
            Text(
                text = randomQuote,
                fontSize = 14.sp,
                color = Color(0xFF6B5FAE),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Snackbar transparan hijau di bawah layar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            snackbar = { snackbarData ->
                Surface(
                    color = Color(0xFFE6F2FF).copy(alpha = 0.95f),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        color = Color(0xFF1565C0),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        )
    }
}
