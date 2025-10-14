package com.example.testing.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HelpSupportScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔙 Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(Color.White.copy(alpha = 0.8f))
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF8B4CFC)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Help & Support",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF4B4453)
                )
            }

            // 🧭 Header Icon
            Icon(
                imageVector = Icons.Default.Help,
                contentDescription = null,
                tint = Color(0xFF8B4CFC),
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Need some help?",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF4B4453)
            )
            Text(
                "Find answers to common questions or contact us directly.",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 📘 FAQ Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Frequently Asked Questions",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B4CFC),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FAQItem(
                        question = "How do I edit my profile?",
                        answer = "You can edit your profile by tapping the 'Edit Profile Information' option in the Profile tab."
                    )

                    FAQItem(
                        question = "How can I enable daily reminders?",
                        answer = "Toggle the 'Notifications' switch in the Profile page to receive daily journal reminders."
                    )

                    FAQItem(
                        question = "I forgot my password. What should I do?",
                        answer = "You can reset your password from the Sign In page by choosing 'Forgot Password'."
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 📬 Contact Support
            Button(
                onClick = { /* TODO: open email or support form */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4CFC)),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Icon(Icons.Default.MailOutline, contentDescription = "Email", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contact Support", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(question, fontWeight = FontWeight.SemiBold, color = Color.Black)
        Text(
            answer,
            color = Color.Gray,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
        Divider(color = Color(0xFFF0EAFD), thickness = 1.dp, modifier = Modifier.padding(top = 8.dp))
    }
}
