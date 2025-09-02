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
    val icon: ImageVector
) {
    object Normal : CaffeineBand("Normal", Color(0xFF70B058), Icons.Filled.CheckCircleOutline)
    object Caution : CaffeineBand("Caution", Color(0xFFF3A800), Icons.Filled.ErrorOutline)
    object Warning : CaffeineBand("Warning", Color(0xFFE53935), Icons.Filled.WarningAmber)
    
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
