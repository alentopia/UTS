@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.testing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.testing.ui.screens.MainNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()
            val gradientTop = Color(0xFFEED3F2)
            val gradientBottom = Color(0xFFD1E5FF)

            // 🟣 Atur warna status & nav bar supaya nyatu dengan background gradient
            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
                systemUiController.setNavigationBarColor(
                    color = gradientBottom.copy(alpha = 0.8f),
                    darkIcons = useDarkIcons
                )
            }

            MaterialTheme(
                colorScheme = lightColorScheme(
                    background = Color.Transparent
                )
            ) {
                MainNavHost()
            }
        }
    }
}
