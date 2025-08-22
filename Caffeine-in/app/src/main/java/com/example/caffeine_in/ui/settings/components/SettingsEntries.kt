package com.example.caffeine_in.ui.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.caffeine_in.data.SettingItem

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFF967259),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}


@Composable
fun SettingsSection(
    items: List<SettingItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFD8C9BA))
    ) {
        items.forEachIndexed { index, item ->
            SettingsRow(
                item = item,
                isFirst = index == 0,
                isLast = index == items.lastIndex,
                showDivider = index < items.lastIndex
            )
        }
    }
}

@Composable
fun SettingsRow(
    item: SettingItem,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    showDivider: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = item.onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color(0xFF38220F)
                )
                item.subtitle?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color(0xFF967259),
                        modifier = Modifier.padding(top = 2.dp)
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
        
        if (showDivider) {
            HorizontalDivider(
                color = Color(0x3F967259),
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
