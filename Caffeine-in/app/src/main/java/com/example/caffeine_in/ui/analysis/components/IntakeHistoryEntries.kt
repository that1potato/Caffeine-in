package com.example.caffeine_in.ui.analysis.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caffeine_in.data.CaffeineIntake
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IntakeHistoryItem(
    intake: CaffeineIntake,
    modifier: Modifier = Modifier
) {
    val dateFormatter = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = intake.sourceName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF38220F)
            )
            Text(
                text = "${intake.amount}mg",
                fontSize = 14.sp,
                color = Color(0xFF967259)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = dateFormatter.format(Date(intake.timestampMillis)),
            fontSize = 14.sp,
            color = Color(0xFF967259),
            fontWeight = FontWeight.Medium
        )
    }
}
