package com.example.caffeine_in.ui.info.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caffeine_in.caffeineBand.CaffeineBand

@Composable
fun Indicator() {
    val caffeineBands = listOf(CaffeineBand.Normal, CaffeineBand.Caution, CaffeineBand.Warning)
    
    Column(verticalArrangement = Arrangement.spacedBy(12.dp) ) {
        caffeineBands.forEach { band ->
            IndicatorRow(band = band)
        }
    }
}

@Composable
fun IndicatorRow(band: CaffeineBand) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                band.color,
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFECE0D1))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = band.icon,
                tint = band.color,
                contentDescription = "${band.name} Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = band.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF38220F)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = band.range,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF38220F)
            )
        }
    }
}
