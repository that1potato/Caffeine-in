package com.example.caffeine_in.ui.info.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    Normal()
    Spacer(modifier = Modifier.height(12.dp))
    Caution()
    Spacer(modifier = Modifier.height(12.dp))
    Warning()
}

@Composable
fun Normal() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                CaffeineBand.Normal.color,
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Normal  ❘ ≤ 400mg/24h",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF38220F)
                )
            }
            Icon(
                imageVector = CaffeineBand.Normal.icon,
                tint = CaffeineBand.Normal.color,
                contentDescription = "Normal Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun Caution() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                CaffeineBand.Caution.color,
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Caution ❘ 400~600mg/24h",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF38220F)
                )
            }
            Icon(
                imageVector = CaffeineBand.Caution.icon,
                tint = CaffeineBand.Caution.color,
                contentDescription = "Normal Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun Warning() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                CaffeineBand.Warning.color,
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Warning ❘ > 600mg/24h",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF38220F)
                )
            }
            Icon(
                imageVector = CaffeineBand.Warning.icon,
                tint = CaffeineBand.Warning.color,
                contentDescription = "Normal Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
