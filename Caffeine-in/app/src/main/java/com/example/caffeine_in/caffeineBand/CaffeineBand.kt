package com.example.caffeine_in.caffeineBand

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class CaffeineBand(
    val name: String,
    val color: Color,
    val icon: ImageVector,
    val range: String,
    val description: String
) {
    object Normal :
        CaffeineBand(
            name = "NORMAL",
            color = Color(0xFF70B058),
            icon = Icons.Filled.CheckCircleOutline,
            range = "≤ 400mg/24h",
            description = "Your caffeine intake is within the FDA recommend range."
        )
    object Caution :
        CaffeineBand(
            name = "CAUTION",
            color = Color(0xFFF3A800),
            icon = Icons.Filled.ErrorOutline,
            range = "400~600mg/24h",
            description = "Your caffeine intake is over the FDA recommended range."
        )
    object Warning :
        CaffeineBand(
            name = "WARNING",
            color = Color(0xFFE53935),
            icon = Icons.Filled.WarningAmber,
            range = "> 600mg/24h",
            description = "Your caffeine intake is significantly higher than the FDA recommended range."
        )
    
    companion object {
        fun getBand(caffeineAmount: Double): CaffeineBand {
            return when {
                caffeineAmount < 400 -> Normal
                caffeineAmount in 400.0..600.0 -> Caution
                else -> Warning
            }
        }
    }
}
