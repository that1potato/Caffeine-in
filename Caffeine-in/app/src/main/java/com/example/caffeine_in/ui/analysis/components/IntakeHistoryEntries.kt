package com.example.caffeine_in.ui.analysis.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caffeine_in.data.CaffeineIntake
import com.example.caffeine_in.data.SettingItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PerDaySection(
    date: String,
    intakes: List<CaffeineIntake>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFECE0D1)),
        border = BorderStroke(width = 1.dp, color = Color(0xFF967259))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = date,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF38220F),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            intakes.forEachIndexed { index, intake ->
                IntakeHistoryItem(intake = intake)
                if (index < intakes.lastIndex) {
                    HorizontalDivider(
                        color = Color(0x3F967259),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun IntakeHistoryItem(
    intake: CaffeineIntake,
    modifier: Modifier = Modifier,
    showDate: Boolean = false
) {
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateTimeFormatter = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = if (showDate) 12.dp else 8.dp),
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
            text = if (showDate) {
                dateTimeFormatter.format(Date(intake.timestampMillis))
            } else {
                timeFormatter.format(Date(intake.timestampMillis))
            },
            fontSize = 14.sp,
            color = Color(0xFF967259),
            fontWeight = FontWeight.Medium
        )
    }
}
