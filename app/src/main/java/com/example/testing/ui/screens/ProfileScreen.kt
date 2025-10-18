package com.example.testing.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
        systemUiController.setNavigationBarColor(Color.Transparent, darkIcons = true)
    }

    val firebaseAuth = FirebaseAuth.getInstance()
    var showLogoutDialog by remember { mutableStateOf(false) }


    // MAIN CONTENT
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(top = 32.dp, bottom = 64.dp)
        ) {
            // Avatar Section
            item {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color(0xFFDCCBFF),
                                        Color(0xFFEADFFB)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = "https://cdn-icons-png.flaticon.com/512/847/847969.png"
                            ),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2D2D4F))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "USER",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF2C2C2C)
                )
                Text(
                    "user.hehehe@email.com | +62 812 3456 7890",
                    color = Color(0xFF7A7A7A),
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(17.dp))
            }



            //  Profile Settings
            item {
                ProfileCard(
                    title = "Profile Settings",
                    options = listOf(
                        SettingItem(Icons.Default.Edit, "Edit Profile") {
                            navController.navigate("edit_profile")
                        })
                )
            }

            //  Account & Security
            item {
                ProfileCard(
                    title = "Account & Security",
                    options = listOf(
                        SettingItem(Icons.Default.Security, "Manage Account") {
                            navController.navigate("account_security")
                        }
                    )
                )
            }

            // Support
            item {
                ProfileCard(
                    title = "Support",
                    options = listOf(
                        SettingItem(Icons.Default.Help, "Help & Support") {
                            navController.navigate("help_support")
                        }
                    )
                )
            }

            // Logout Button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { showLogoutDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(
                        Icons.Default.Logout,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    //  Logout Dialog Overlay
    if (showLogoutDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .systemBarsPadding()
                .zIndex(999f), // di atas semua
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Logout Confirmation",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF222222)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Are you sure you want to log out?",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TextButton(
                            onClick = { showLogoutDialog = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = Color.Gray)
                        }
                        Button(
                            onClick = {
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

@Composable
fun ProfileCard(
    title: String,
    options: List<SettingItem>,
) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF222222),
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                options.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { item.onClick() }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                item.icon,
                                contentDescription = null,
                                tint = Color(0xFF8B4CFC),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(item.title, color = Color(0xFF333333), fontSize = 15.sp)
                        }
                    }
                    if (index < options.lastIndex)
                        Divider(color = Color(0xFFECE6FF))
                }
            }
        }
    }
}

data class SettingItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val onClick: () -> Unit
)
