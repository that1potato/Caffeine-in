package com.example.caffeine_in.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.caffeine_in.ui.analysis.AnalysisScreen
import com.example.caffeine_in.ui.caffeinetracker.CaffeineTrackerScreen
import com.example.caffeine_in.ui.caffeinetracker.components.ToolBarFAB
import com.example.caffeine_in.ui.info.InfoScreen
import com.example.caffeine_in.ui.settings.SettingsScreen
import com.example.caffeine_in.ui.settings.LicensesScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CaffeineApp() {
    val navController = rememberNavController()
    val currentDestination =
        navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = Color(0xFFECE0D1),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ToolBarFAB(
                navController = navController,
                currentDestination = currentDestination,
                onFabClick = { navController.navigate("tracker") }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = "tracker",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("tracker") { CaffeineTrackerScreen(navController) }
            composable("analysis") { AnalysisScreen(navController) }
            composable("settings") { SettingsScreen(navController) }
            composable("info") { InfoScreen(navController) }
            composable("licenses") { LicensesScreen(navController) }
        }
    }
}
