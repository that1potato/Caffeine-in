package com.example.caffeine_in.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caffeine_in.data.SettingItem

@Composable
fun SettingsRow(item: SettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = item.onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF38220F)
            )
            item.subtitle?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = Color(0xFF967259)
                )
            }
        }
        if (item.hasSwitch) {
            Switch(
                checked = item.isSwitchEnabled,
                onCheckedChange = item.onSwitchChange
            )
        }
    }
}
