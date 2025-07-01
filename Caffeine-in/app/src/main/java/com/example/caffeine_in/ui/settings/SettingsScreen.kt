package com.example.caffeine_in.ui.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.caffeine_in.data.SettingItem
import com.example.caffeine_in.data.SettingSection
import com.example.caffeine_in.ui.settings.components.License
import com.example.caffeine_in.ui.settings.components.SettingsRow
import com.example.caffeine_in.ui.settings.components.SettingsSectionHeader
import com.example.caffeine_in.ui.settings.components.SettingsTopBar
import com.example.caffeine_in.ui.theme.CaffeineinTheme

@Composable
fun SettingsScreen(navController: NavController) {
    val settingsItems = listOf(
        SettingSection("General"),
        SettingItem("Dark Theme", "Select a theme"),
        SettingItem("Material You Colors", "Turn Material You colors on/off"),

        SettingSection("License")
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
        val modifiedPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
            bottom = 0.dp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(modifiedPadding)
                .padding(horizontal = 32.dp)
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
            item {
                License(navController)
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
