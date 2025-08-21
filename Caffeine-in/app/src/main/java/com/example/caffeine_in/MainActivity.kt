package com.example.caffeine_in

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caffeine_in.navigation.Destination
import com.example.caffeine_in.ui.analysis.AnalysisScreen
import com.example.caffeine_in.ui.caffeinetracker.CaffeineTrackerScreen
import com.example.caffeine_in.ui.info.InfoScreen
import com.example.caffeine_in.ui.settings.LicensesScreen
import com.example.caffeine_in.ui.settings.SettingsScreen
import com.example.caffeine_in.ui.theme.CaffeineinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaffeineinTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Destination.Tracker.route) {
                        composable(Destination.Tracker.route) {
                            CaffeineTrackerScreen(navController = navController)
                        }
                        composable(Destination.Info.route) {
                            InfoScreen(navController = navController)
                        }
                        composable(Destination.Analysis.route) {
                            AnalysisScreen(navController = navController)
                        }
                        composable(Destination.Settings.route) {
                            SettingsScreen(navController = navController)
                        }
                        composable(Destination.Licenses.route) {
                            LicensesScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}