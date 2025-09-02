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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.WarningAmber
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
                Color(0xFF70B058),
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
                imageVector = Icons.Filled.CheckCircleOutline,
                tint = Color(0xFF70B058),
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
                Color(0xFFF3A800),
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
                imageVector = Icons.Filled.ErrorOutline,
                tint = Color(0xFFF3A800),
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
                Color(0xFFE53935),
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
                imageVector = Icons.Filled.WarningAmber,
                tint = Color(0xFFE53935),
                contentDescription = "Normal Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
