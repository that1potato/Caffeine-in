package com.example.caffeine_in.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Approval
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Tracker : Destination(
        route = "tracker",
        label = "Tracker",
        icon = Icons.Outlined.Coffee
    )
    
    data object Analysis : Destination(
        route = "analysis",
        label = "Analysis",
        icon = Icons.Outlined.AutoGraph
    )
    
    data object Settings : Destination(
        route = "settings",
        label = "Settings",
        icon = Icons.Outlined.Settings
    )
    
    data object Info : Destination(
        route = "info",
        label = "Info",
        icon = Icons.Outlined.Info
    )
    
    data object Licenses : Destination(
        route = "licenses",
        label = "Licenses",
        icon = Icons.Outlined.Approval
    )
}

// Navigation items in the toolbar
val toolbarDestinations = listOf(
    Destination.Analysis,
    Destination.Tracker,
    Destination.Settings
)
