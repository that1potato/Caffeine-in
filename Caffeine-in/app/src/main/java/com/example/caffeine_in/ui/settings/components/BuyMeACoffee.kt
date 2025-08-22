package com.example.caffeine_in.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BuyMeACoffee() {
    val uriHandler = LocalUriHandler.current
    val buyMeACoffeeURL = "https://coff.ee/potato987"
    
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            uriHandler.openUri(buyMeACoffeeURL)
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE57825))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Support Me!",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF38220F)
                )
                Text(
                    text = "Buy me a coffee if you enjoyed using this app ( ˘▽˘)っ♨",
                    fontSize = 14.sp,
                    color = Color(0xFF38220F)
                )
            }
        }
    }
}
