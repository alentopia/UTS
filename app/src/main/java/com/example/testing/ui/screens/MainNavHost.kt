package com.example.testing.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.spotifydemo.ui.MusicScreen
import com.example.testing.JurnalModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val listJurnal = remember { mutableStateListOf<JurnalModel>() }
    val systemUiController = rememberSystemUiController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    SideEffect {
        when (currentRoute) {
            "signin", "signup", "forgotpassword" -> {
                systemUiController.setStatusBarColor(Color(0xFF7E63FF), darkIcons = false)
                systemUiController.setNavigationBarColor(Color.White, darkIcons = true)
            }

            else -> {
                systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
                systemUiController.setNavigationBarColor(Color.Transparent, darkIcons = true)
            }
        }
    }

    val gradientColors = when (currentRoute) {
        "signin", "signup", "forgotpassword" -> listOf(Color(0xFF7E63FF), Color.White)
        else -> listOf(Color(0xFFEED3F2), Color(0xFFD1E5FF))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = gradientColors))
    ) {
        //  Konten utama
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                val showBottomNav = currentRoute in listOf(
                    "home",
                    "journal_list",
                    "music",
                    "profile",
                    "mood_calendar"
                )
                if (showBottomNav) BottomNavBar(navController = navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "splash",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("splash") { SplashScreen(navController) }

                //  Auth
                composable("signin") { SignInScreen(navController) }
                composable("signup") { SignUpScreen(navController) }
                composable("forgotpassword") { ForgotPasswordScreen(navController) }

                //  Main
                composable("home") { HomeScreen(navController) }
                composable("music") {
                    MusicScreen(
                        clientId = "9ace244d481f48d5b34acea812017a62",
                        clientSecret = "6bf846d587384642ae370f0700876276"
                    )
                }
                composable("profile") { ProfileScreen(navController = navController) }
                composable("edit_profile") { EditProfileScreen(navController) }
                composable("account_security") { AccountSecurityScreen(navController) }
                composable("help_support") { HelpSupportScreen(navController) }

                // 📒 Journal
                composable("journal_list") {
                    JournalListScreen(
                        navController = navController,
                        listJurnal = listJurnal,
                        onAddClick = { navController.navigate("mood_picker") },
                        onDelete = { index ->
                            if (index in listJurnal.indices) listJurnal.removeAt(index)
                        }
                    )
                }
                composable("mood_calendar") { MoodCalendarScreen(navController) }

                composable("mood_picker") {
                    MoodPickerScreen(
                        onContinue = { emoji, mood ->
                            navController.navigate("write_journal/$emoji/$mood")
                        },
                        navController = navController
                    )
                }

                composable(
                    "write_journal/{emoji}/{mood}",
                    arguments = listOf(
                        navArgument("emoji") { type = NavType.StringType },
                        navArgument("mood") { type = NavType.StringType }
                    )
                ) { backStack ->
                    val emoji = backStack.arguments?.getString("emoji") ?: "😐"
                    val mood = backStack.arguments?.getString("mood") ?: "Neutral"
                    val date =
                        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())

                    WriteJournalScreen(
                        emoji = emoji,
                        mood = mood,
                        date = date,
                        onSave = { title, content, location, dateNow ->
                            listJurnal.add(
                                0,
                                JurnalModel(
                                    emoji = emoji,
                                    mood = mood,
                                    title = title,
                                    content = content,
                                    location = location,
                                    date = dateNow
                                )
                            )
                            navController.navigate("journal_list") {
                                popUpTo("journal_list") { inclusive = true }
                            }
                        },
                        onSkip = { dateNow ->
                            listJurnal.add(
                                0,
                                JurnalModel(
                                    emoji = emoji,
                                    mood = mood,
                                    title = "Untitled",
                                    content = "",
                                    location = "Unknown",
                                    date = dateNow
                                )
                            )
                            navController.navigate("journal_list") {
                                popUpTo("journal_list") { inclusive = true }
                            }
                        },
                        navController = navController
                    )
                }


                composable(
                    "journal_detail/{title}/{content}/{date}/{emoji}/{location}",
                    arguments = listOf(
                        navArgument("title") { type = NavType.StringType },
                        navArgument("content") { type = NavType.StringType },
                        navArgument("date") { type = NavType.StringType },
                        navArgument("emoji") { type = NavType.StringType },
                        navArgument("location") { type = NavType.StringType }
                    )
                ) { entry ->
                    val title = entry.arguments?.getString("title") ?: ""
                    val content = entry.arguments?.getString("content") ?: ""
                    val date = entry.arguments?.getString("date") ?: ""
                    val emoji = entry.arguments?.getString("emoji") ?: ""
                    val location = entry.arguments?.getString("location") ?: ""

                    JournalDetailScreen(
                        title = title,
                        content = content,
                        date = date,
                        emoji = emoji,
                        location = location,
                        navController = navController
                    )
                }

                composable(
                    "edit_journal/{title}/{content}/{date}/{emoji}/{location}",
                    arguments = listOf(
                        navArgument("title") { type = NavType.StringType },
                        navArgument("content") { type = NavType.StringType },
                        navArgument("date") { type = NavType.StringType },
                        navArgument("emoji") { type = NavType.StringType },
                        navArgument("location") { type = NavType.StringType }
                    )
                ) { entry ->
                    val title = entry.arguments?.getString("title") ?: ""
                    val content = entry.arguments?.getString("content") ?: ""
                    val date = entry.arguments?.getString("date") ?: ""
                    val emoji = entry.arguments?.getString("emoji") ?: ""
                    val location = entry.arguments?.getString("location") ?: ""

                    EditJournalScreen(
                        title = title,
                        content = content,
                        date = date,
                        emoji = emoji,
                        location = location,
                        navController = navController
                    )
                }

            }
        }
    }
}



