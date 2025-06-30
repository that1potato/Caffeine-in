package com.example.caffeine_in.ui.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.caffeine_in.ui.theme.CaffeineinTheme

@Composable
fun SettingsScreen(navController: NavController) {
    Text("Settings Screen")
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun DefaultPreview() {
    CaffeineinTheme {
        SettingsScreen(navController = rememberNavController())
    }
}
