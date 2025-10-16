package com.example.testing.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun FullScreenOverlay(
    visible: Boolean,
    backgroundColor: Color = Color.Black.copy(alpha = 0.45f),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = contentAlignment
        ) {
            content()
        }
    }
}
