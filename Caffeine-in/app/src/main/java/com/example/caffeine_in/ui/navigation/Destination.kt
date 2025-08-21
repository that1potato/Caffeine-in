package com.example.caffeine_in.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector = selectedIcon
) {
    data object Tracker : Destination(
        route = "tracker",
        label = "Track",
        selectedIcon = Icons.Rounded.Coffee
    )
    data object Analysis : Destination(
        route = "analysis",
        label = "Analysis",
        selectedIcon = Icons.Rounded.BarChart
    )
    data object Settings : Destination(
        route = "settings",
        label = "Settings",
        selectedIcon = Icons.Rounded.Settings
    )
}

val bottomDestinations = listOf(
    Destination.Tracker,
    Destination.Analysis,
    Destination.Settings
)

class FloatingNavbar {
}