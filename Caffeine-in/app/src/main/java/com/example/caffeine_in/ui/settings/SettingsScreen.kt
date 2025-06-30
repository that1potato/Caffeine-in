package com.example.caffeine_in.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.caffeine_in.data.SettingItem
import com.example.caffeine_in.data.SettingSection
import com.example.caffeine_in.ui.settings.components.SettingsRow
import com.example.caffeine_in.ui.settings.components.SettingsSectionHeader
import com.example.caffeine_in.ui.settings.components.SettingsTopBar
import com.example.caffeine_in.ui.theme.CaffeineinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var bubblesEnabled by remember { mutableStateOf(false) }

    val settingsItems = listOf(
        SettingSection("Manage"),
        SettingItem("App notifications", "Control notifications from individual apps"),
        SettingItem("Notification history", "Show recent and snoozed notifications"),
        SettingSection("Conversation"),
        SettingItem("Conversations", "21 priority conversations"),
        SettingItem("Bubbles", if (bubblesEnabled) "On" else "Off", hasSwitch = true, isSwitchEnabled = bubblesEnabled, onSwitchChange = { bubblesEnabled = it }),
        SettingSection("Privacy"),
        SettingItem("Notification read, reply & control", "Control which apps and devices can read notifications"),
        SettingItem("Notifications on lock screen"),
        SettingSection("Sync across devices"),
        SettingItem("Dismiss notifications across Pixel devices", "Notifications dismissed on your Pixel phone or tablet will no longer appear on both"),
    )

    Scaffold(
        containerColor = Color(0xFFECE0D1),
        topBar = {
            SettingsTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(settingsItems.size) { index ->
                when (val item = settingsItems[index]) {
                    is SettingSection -> {
                        SettingsSectionHeader(title = item.title)
                    }
                    is SettingItem -> {
                        SettingsRow(item = item)
                        if (index < settingsItems.lastIndex && settingsItems[index + 1] !is SettingSection) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun DefaultPreview() {
    CaffeineinTheme {
        SettingsScreen(navController = rememberNavController())
    }
}
