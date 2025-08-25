package com.example.caffeine_in

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.caffeine_in.navigation.AnimatedFloatingNavBar
import com.example.caffeine_in.navigation.Destination
import com.example.caffeine_in.ui.analysis.AnalysisScreen
import com.example.caffeine_in.ui.caffeinetracker.CaffeineTrackerScreen
import com.example.caffeine_in.ui.info.InfoScreen
import com.example.caffeine_in.ui.settings.LicensesScreen
import com.example.caffeine_in.ui.settings.SettingsScreen
import com.example.caffeine_in.ui.theme.CaffeineinTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaffeineinTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                val showFab = currentRoute == Destination.Tracker.route
                var showAddDialog by remember { mutableStateOf(false) }
                
                val showNavBar = currentRoute in listOf(
                    Destination.Tracker.route,
                    Destination.Analysis.route,
                    Destination.Settings.route
                )
                
                Scaffold(
                    containerColor = Color(0xFFECE0D1),
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        AnimatedVisibility(
                            visible = showNavBar,
                            enter = slideInVertically(
                                animationSpec = tween(300),
                                initialOffsetY = { it }
                            ),
                            exit = slideOutVertically(
                                animationSpec = tween(300),
                                targetOffsetY = { it }
                            ) + fadeOut(tween(300))
                        ) {
                            AnimatedFloatingNavBar(
                                navController = navController,
                                currentRoute = currentRoute ?: "",
                                showFab = showFab,
                                onFabClick = { showAddDialog = true }
                            )
                        }
                    }
                ) { innerPadding ->
                    val modifiedPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        bottom = 0.dp
                    )
                    
                    NavHost(
                        navController = navController,
                        startDestination = Destination.Tracker.route,
                    ) {
                        composable(Destination.Tracker.route) {
                            CaffeineTrackerScreen(
                                navController = navController,
                                paddingValues = modifiedPadding,
                                showAddDialog = showAddDialog,
                                onDismissDialog = { showAddDialog = false }
                            )
                        }
                        composable(Destination.Analysis.route) {
                            AnalysisScreen(
                                navController = navController
                            )
                        }
                        composable(Destination.Settings.route) {
                            SettingsScreen(
                                navController = navController
                            )
                        }
                        composable(Destination.Info.route) {
                            InfoScreen(navController = navController)
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
