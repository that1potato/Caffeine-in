package com.example.caffeine_in.ui.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caffeine_in.data.SettingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    modifier: Modifier,
    navController: NavController
) {
    val textIconColor = Color(0xFF38220F)
    val iconModifier = Modifier.size(24.dp)

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(Color(0xFFECE0D1)),
        title = {
            Text(
                text = "Settings",
                fontWeight = FontWeight.Bold,
                color = textIconColor
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(
                    modifier = iconModifier,
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = textIconColor
                )
            }
        }
    )
}

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

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFF967259),
        //textDecoration = TextDecoration.Underline,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun License() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFECE0D1)),
        border = BorderStroke(width = 1.dp, color = Color(0xFF967259))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Caffeine-In",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF38220F)
                )
                Text(
                    text = "Copyright (c) 2025 Potato987.",
                    fontSize = 14.sp,
                    color = Color(0xFF38220F)
                )
            }
        }
    }
}

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