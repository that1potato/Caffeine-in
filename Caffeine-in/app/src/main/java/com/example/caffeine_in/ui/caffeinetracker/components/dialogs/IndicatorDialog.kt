package com.example.caffeine_in.ui.caffeinetracker.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.caffeine_in.caffeineBand.CaffeineBand
import com.example.caffeine_in.ui.theme.FiraCodeFontFamily

@Composable
fun IndicatorDialog(
    currentBand: CaffeineBand,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        containerColor = Color(0xFFECE0D1),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = currentBand.name,
                color = Color(0xFF38220F),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = currentBand.description,
                color = Color(0xFF38220F),
                fontSize = 16.sp,
                fontFamily = FiraCodeFontFamily,
                fontWeight = FontWeight.Normal
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38220F))
            ) {
                Text(
                    text = "Learn More",
                    color = Color(0xFFECE0D1)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Dismiss",
                    color = Color(0xFF38220F)
                )
            }
        }
    )
}
