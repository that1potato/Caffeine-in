package com.example.caffeine_in.ui.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFF967259),
        //textDecoration = TextDecoration.Underline,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp)
    )
}
