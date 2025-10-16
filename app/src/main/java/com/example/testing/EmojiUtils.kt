package com.example.testing

import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testing.R

// 🔹 Mapping dari emoji ke drawable (biar gak ubah data lama)
private val emojiToDrawable = mapOf(
    "😡" to R.drawable.angry,
    "😔" to R.drawable.sad,
    "😐" to R.drawable.neutral,
    "😊" to R.drawable.happy,
    "😍" to R.drawable.suprise,
    "😱" to R.drawable.fear
)

/**
 * EmojiSmart composable
 * Menampilkan emoji sebagai gambar kalau ada drawable-nya,
 * kalau gak ada fallback ke emoji teks biasa 😆
 */
@Composable
fun EmojiSmart(
    emoji: String,
    modifier: Modifier = Modifier
) {
    val resId = emojiToDrawable[emoji]
    if (resId != null) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = emoji,
            modifier = modifier
        )
    } else {
        Text(
            text = emoji,
            fontSize = 28.sp,
            modifier = modifier
        )
    }
}
