package com.example.caffeine_in.ui.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.caffeine_in.data.SettingItem
import com.example.caffeine_in.data.SettingSection
import com.example.caffeine_in.navigation.Destination
import com.example.caffeine_in.ui.settings.components.License
import com.example.caffeine_in.ui.settings.components.SettingsRow
import com.example.caffeine_in.ui.settings.components.SettingsSectionHeader
import com.example.caffeine_in.ui.settings.components.SettingsTopBar
import com.example.caffeine_in.ui.settings.components.BuyMeACoffee
import com.example.caffeine_in.ui.theme.CaffeineinTheme

@Composable
fun SettingsScreen(
    navController: NavController,
    paddingValues: PaddingValues = PaddingValues()
) {
    val settingsItems = listOf(
        SettingSection("General"),
        //SettingItem("Dark Theme", "Dark mode behaviour"),
        //SettingItem("Material You Colors", "Turn Material You colors on/off"),
        //SettingItem("Bedtime Schedule", "Set your bedtime schedule"),
        SettingItem(
            "About Caffeine Level",
            "Tap to learn more about how the app helps track your caffeine level",
            onClick = { navController.navigate(Destination.Info.route) }
        ),
        
        //SettingSection("Notification"),
        //SettingItem("HAHAHA"),
        
        //SettingSection("Widget"),
        //SettingItem("HAHAHA"),
        
        //SettingSection("Data & Privacy"),
        //SettingItem("Reset Caffeine Level", "Reset caffeine level to 0mg"),
        //SettingItem("Privacy Statement"),

        SettingSection("License"),
        SettingItem(
            "Licenses", "Tap to open licenses",
            onClick = { navController.navigate(Destination.Licenses.route) }
        )
    )
    
    Scaffold(
        containerColor = Color(0xFFECE0D1),
        topBar = {
            SettingsTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            item {
                BuyMeACoffee()
            }
            items(settingsItems.size) { index ->
                when (val item = settingsItems[index]) {
                    is SettingSection -> {
                        SettingsSectionHeader(title = item.title)
                    }
                    is SettingItem -> {
                        SettingsRow(item = item)
                        if (index < settingsItems.lastIndex && settingsItems[index + 1] !is SettingSection) {
                            HorizontalDivider(color = Color(0x5F967259))
                        }
                    }
                }
            }
            item {
                License()
            }
            item {
                Spacer(modifier = Modifier.height(96.dp))
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
