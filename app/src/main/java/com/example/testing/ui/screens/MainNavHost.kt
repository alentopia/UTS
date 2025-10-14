package com.example.testing.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.testing.JurnalModel
import com.example.testing.ui.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val listJurnal = remember { mutableStateListOf<JurnalModel>() }
    var isDarkTheme by remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()

    // 🔍 Pantau route aktif
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // 🟣 Ganti warna status & nav bar berdasarkan route
    SideEffect {
        when (currentRoute) {
            "signin", "signup", "forgotpassword" -> {
                // 💜 Warna solid untuk halaman auth
                systemUiController.setStatusBarColor(
                    color = Color(0xFF7E63FF),
                    darkIcons = false
                )
                systemUiController.setNavigationBarColor(
                    color = Color.White,
                    darkIcons = true
                )
            }

            else -> {
                // 🌈 Warna transparan untuk halaman lain
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkTheme
                )
                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkTheme
                )
            }
        }
    }




    // 🟣 Bungkus semua di theme
    AppTheme(darkTheme = isDarkTheme) {
        // Tentukan gradient background berdasarkan halaman
        val gradientColors = when (currentRoute) {
            "signin", "signup" -> listOf(
                Color(0xFF7E63FF), // ungu atas
                Color(0xFFFFFFFF)  // putih bawah
            )
            else -> if (isDarkTheme)
                listOf(Color(0xFF1E1E2E), Color(0xFF121212))
            else
                listOf(Color(0xFFEED3F2), Color(0xFFD1E5FF))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = gradientColors))
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                bottomBar = {
                    val showBottomNav = currentRoute in listOf("home", "journal_list", "music", "profile")
                    if (showBottomNav) BottomNavBar(navController = navController)
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "splash",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    // Splash
                    composable("splash") { SplashScreen(navController) }

                    // Auth
                    composable("signin") { SignInScreen(navController) }
                    composable("signup") { SignUpScreen(navController) }
                    composable("forgotpassword") { ForgotPasswordScreen(navController) }

                    // Main pages
                    composable("home") { HomeScreen(navController) }
                    composable("music") { MusicScreen() }
                    composable("profile") {
                        ProfileScreen(
                            navController = navController,
                            isDarkTheme = isDarkTheme,
                            onThemeChange = { newTheme -> isDarkTheme = newTheme }
                        )
                    }
                    composable("edit_profile") { EditProfileScreen(navController) }
                    composable("help_support") { HelpSupportScreen(navController) }

                    // Journal list
                    composable("journal_list") {
                        JournalListScreen(
                            listJurnal = listJurnal,
                            onAddClick = { navController.navigate("mood_picker") },
                            onDelete = { index ->
                                if (index in listJurnal.indices) listJurnal.removeAt(index)
                            }
                        )
                    }

                    // Mood picker
                    composable("mood_picker") {
                        MoodPickerScreen(
                            onContinue = { emoji, mood ->
                                navController.navigate("write_journal/$emoji/$mood")
                            },
                            navController = navController
                        )
                    }

                    // Write journal
                    composable(
                        "write_journal/{emoji}/{mood}",
                        arguments = listOf(
                            navArgument("emoji") { type = NavType.StringType },
                            navArgument("mood") { type = NavType.StringType }
                        )
                    ) { backStack ->
                        val emoji = backStack.arguments?.getString("emoji") ?: "😐"
                        val mood = backStack.arguments?.getString("mood") ?: "Neutral"
                        val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())

                        WriteJournalScreen(
                            emoji = emoji,
                            mood = mood,
                            date = date,
                            onSave = { content, dateNow ->
                                listJurnal.add(0, JurnalModel(emoji, mood, content, dateNow))
                                navController.navigate("journal_list") {
                                    popUpTo("journal_list") { inclusive = true }
                                }
                            },
                            onSkip = { dateNow ->
                                listJurnal.add(0, JurnalModel(emoji, mood, "", dateNow))
                                navController.navigate("journal_list") {
                                    popUpTo("journal_list") { inclusive = true }
                                }
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
