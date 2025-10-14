package com.example.testing.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // 🌈 Background gradient — sama kayak SignInScreen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7E63FF),
                        Color(0xFF9A7BFF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Top Section — biar harmonis dengan style login
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f)
                    .padding(top = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Forgot", color = Color.White.copy(alpha = 0.8f))
                Text(
                    "Your Password?",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "We'll send a reset link to your email.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }

            // 🔸 Bottom Section — card putih bulat kayak login
            Surface(
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Reset Your Password",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A2E6E),
                        fontSize = 18.sp
                    )
                    Text(
                        "Enter your registered email below to get a password reset link.",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    // ✉️ Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7E63FF),
                            unfocusedBorderColor = Color(0xFFD5CFFF)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 🔹 Send Reset Link Button
                    Button(
                        onClick = {
                            if (email.isNotBlank()) {
                                isLoading = true
                                scope.launch {
                                    firebaseAuth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener { task ->
                                            isLoading = false
                                            message = if (task.isSuccessful) {
                                                "✅ Password reset link sent to your email!"
                                            } else {
                                                "⚠️ Failed to send reset link. Please check your email."
                                            }
                                        }
                                }
                            } else {
                                message = "Please enter your email."
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E63FF)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text("Send Reset Link", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    // ⚠️ Status Message
                    message?.let {
                        Text(
                            it,
                            color = if (it.startsWith("✅")) Color(0xFF00C853) else Color.Red,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 🔙 Back to Login
                    TextButton(
                        onClick = { navController.navigate("signin") },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Back to Login", color = Color(0xFF7E63FF), fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
