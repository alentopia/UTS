package com.example.testing.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AccountSecurityScreen(navController: NavController) {
    val firebaseAuth = FirebaseAuth.getInstance()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showVerificationDialog by remember { mutableStateOf(false) }
    var showGoodbyeDialog by remember { mutableStateOf(false) }
    var verificationCode by remember { mutableStateOf("") }
    var enteredCode by remember { mutableStateOf("") }

    // Background utama
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF5E4AE3)
                    )
                }
                Text(
                    text = "Account & Security",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D2D2D)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            //  Security toggles (compact style)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    SecurityToggleCompact("Biometric ID")
                    Divider(color = Color(0xFFF0E9FF))
                    SecurityToggleCompact("Face ID")
                    Divider(color = Color(0xFFF0E9FF))
                    SecurityToggleCompact("SMS Authenticator")
                    Divider(color = Color(0xFFF0E9FF))
                    SecurityToggleCompact("Google Authenticator")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            //  Delete Account Section
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDeleteDialog = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Delete Account",
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Permanently remove your account and data.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFD32F2F))
                }
            }
        }

        //  Blur overlay background for dialogs
        if (showDeleteDialog || showVerificationDialog || showGoodbyeDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .blur(12.dp) //  blur efek lembut
            )
        }

        //  Step 1 — Request verification
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        verificationCode = (100000..999999).random().toString()
                        showVerificationDialog = true
                        showDeleteDialog = false
                    }) {
                        Text("Send Code", color = Color(0xFF8B4CFC), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Delete Account") },
                text = {
                    Text(
                        "We’ll send a 6-digit verification code to your registered email to confirm this action.",
                        color = Color(0xFF555555)
                    )
                }
            )
        }

        //  Step 2 — Enter code
        if (showVerificationDialog) {
            AlertDialog(
                onDismissRequest = { showVerificationDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        if (enteredCode == verificationCode) {
                            showVerificationDialog = false
                            showGoodbyeDialog = true
                        }
                    }) {
                        Text("Verify", color = Color(0xFF8B4CFC), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showVerificationDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Enter Verification Code") },
                text = {
                    OutlinedTextField(
                        value = enteredCode,
                        onValueChange = { enteredCode = it },
                        label = { Text("6-digit Code") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }

        //  Step 3 — Goodbye popup
        if (showGoodbyeDialog) {
            AlertDialog(
                onDismissRequest = { showGoodbyeDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        firebaseAuth.currentUser?.delete()
                        navController.navigate("signin") {
                            popUpTo("home") { inclusive = true }
                        }
                    }) {
                        Text("Yes, delete my account", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showGoodbyeDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("We’ll miss you 💜", textAlign = TextAlign.Center) },
                text = {
                    Text(
                        "Are you sure you want to delete your account?\nAll your journals and data will be permanently removed.",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF555555)
                    )
                }
            )
        }
    }
}

@Composable
fun SecurityToggleCompact(title: String) {
    var enabled by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 14.sp, color = Color(0xFF333333))
        Switch(
            checked = enabled,
            onCheckedChange = { enabled = it },
            modifier = Modifier.scale(0.85f),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF8B4CFC),
                uncheckedThumbColor = Color(0xFFE0E0E0),
                uncheckedTrackColor = Color(0xFFF5F5F5)
            )
        )
    }
}
