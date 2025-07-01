package com.example.caffeine_in

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caffeine_in.ui.caffeinetracker.CaffeineTrackerScreen
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
                    NavHost(navController = navController, startDestination = "tracker") {
                        composable("tracker") {
                            CaffeineTrackerScreen(navController = navController)
                        }
                        composable("settings") {
                            SettingsScreen(navController = navController)
                        }
                        composable("licenses") {
                            LicensesScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}