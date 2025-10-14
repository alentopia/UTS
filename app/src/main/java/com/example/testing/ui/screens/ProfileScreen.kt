package com.example.testing.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = !isDarkTheme)
        systemUiController.setNavigationBarColor(Color.Transparent, darkIcons = !isDarkTheme)
    }

    val firebaseAuth = FirebaseAuth.getInstance()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showNotifPopup by remember { mutableStateOf(false) }

    val bgGradient = if (isDarkTheme) {
        Brush.verticalGradient(listOf(Color(0xFF2E2E3A), Color(0xFF1B1B25)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFEADFFB), Color(0xFFD9E8FF)))
    }

    val textColor = if (isDarkTheme) Color.White else Color.Black
    val cardColor = if (isDarkTheme) Color(0xFF2A2A3B) else Color.White

    // 🌈 Background utama
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(horizontal = 24.dp)
    ) {
        // 📱 Konten utama
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 32.dp, bottom = 64.dp)
        ) {
            // 👤 Avatar Section
            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF8B4CFC).copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        tint = Color(0xFF8B4CFC),
                        modifier = Modifier.size(60.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF8B4CFC))
                            .clickable { navController.navigate("edit_profile") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Valen Angellina", fontWeight = FontWeight.Bold, color = textColor)
                Text(
                    "valen.angellina@email.com | +62 812 3456 7890",
                    color = if (isDarkTheme) Color.LightGray else Color.Gray,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ⚙️ Section 1 — Profile Settings
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        // ✏️ Edit profile
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("edit_profile") }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF8B4CFC))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Edit profile information", color = textColor)
                            }
                        }

                        Divider(color = Color(0xFF4E4E5A).copy(alpha = 0.2f))

                        // 🔔 Notifications toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF8B4CFC))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Notifications", color = textColor)
                            }
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { isChecked ->
                                    notificationsEnabled = isChecked
                                    showNotifPopup = true
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF8B4CFC)),
                                modifier = Modifier.scale(0.9f)
                            )
                        }
                    }
                }
            }

            // 🌗 Section 2 — Others
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        // 🌙 Theme toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DarkMode, contentDescription = null, tint = Color(0xFF8B4CFC))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Dark Mode", color = textColor)
                            }
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = { onThemeChange(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF8B4CFC)),
                                modifier = Modifier.scale(0.9f)
                            )
                        }

                        Divider(color = Color(0xFF4E4E5A).copy(alpha = 0.2f))

                        // ❓Help
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("help_support") }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Help, contentDescription = null, tint = Color(0xFF8B4CFC))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Help & Support", color = textColor)
                            }
                        }
                    }
                }
            }

            // 🚪 Logout Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showLogoutDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 🔔 Popup Notification
        if (showNotifPopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .wrapContentHeight()
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            if (notificationsEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                            contentDescription = null,
                            tint = Color(0xFF8B4CFC),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (notificationsEnabled) "Notifications Enabled" else "Notifications Disabled",
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            if (notificationsEnabled)
                                "You’ll now receive daily journal reminders."
                            else
                                "You’ll no longer receive journal notifications.",
                            color = if (isDarkTheme) Color.LightGray else Color.Gray,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Auto close
            LaunchedEffect(notificationsEnabled) {
                delay(2000)
                showNotifPopup = false
            }
        }

        // 🚪 Logout Confirmation Dialog
        if (showLogoutDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Logout Confirmation", fontWeight = FontWeight.Bold, color = textColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Are you sure you want to log out?", color = if (isDarkTheme) Color.LightGray else Color.Gray, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            TextButton(onClick = { showLogoutDialog = false }, modifier = Modifier.weight(1f)) {
                                Text("Cancel", color = if (isDarkTheme) Color.LightGray else Color.Gray)
                            }
                            Button(
                                onClick = {
                                    showLogoutDialog = false
                                    firebaseAuth.signOut()
                                    navController.navigate("signin") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4CFC)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Yes", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
