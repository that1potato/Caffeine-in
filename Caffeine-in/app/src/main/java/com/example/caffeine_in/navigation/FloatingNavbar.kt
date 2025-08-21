package com.example.caffeine_in.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingToolbarColors
import androidx.compose.material3.FloatingToolbarHorizontalFabPosition
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@ExperimentalMaterial3ExpressiveApi
@Composable
fun ToolBarFAB(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentRoute: String,
    showFab: Boolean = false,
    onFabClick: () -> Unit = {},
    expanded: Boolean = true
) {
    val toolbarColors = FloatingToolbarColors(
        toolbarContainerColor = Color(0xFFC5B5A6),
        toolbarContentColor = Color(0xFF38220F),
        fabContainerColor = Color(0xFFE57825),
        fabContentColor = Color(0xFF38220F)
    )
    
    if (showFab) {
        HorizontalFloatingToolbar(
            modifier = modifier,
            expanded = expanded,
            colors = toolbarColors,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onFabClick,
                    containerColor = Color(0xFFE57825),
                    contentColor = Color(0xFF38220F)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Caffeine"
                    )
                }
            },
            floatingActionButtonPosition = FloatingToolbarHorizontalFabPosition.End,
            content = {
                NavigationButtons(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        )
    } else {
        HorizontalFloatingToolbar(
            modifier = modifier,
            expanded = expanded,
            colors = toolbarColors,
            content = {
                NavigationButtons(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        )
    }
}

@Composable
private fun NavigationButtons(
    navController: NavController,
    currentRoute: String
) {
    // Helper function to determine button colors based on selection
    @Composable
    fun getButtonColors(isSelected: Boolean): IconButtonColors {
        return if (isSelected) {
            IconButtonColors(
                containerColor = Color(0xFF38220F),
                contentColor = Color(0xFFECE0D1),
                disabledContainerColor = Color(0xFF38220F),
                disabledContentColor = Color(0xFFECE0D1)
            )
        } else {
            IconButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF38220F),
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color(0xFF38220F)
            )
        }
    }
    
    // Helper function to get icon tint based on selection
    fun getIconTint(isSelected: Boolean): Color {
        return if (isSelected) Color(0xFFECE0D1) else Color(0xFF38220F)
    }
    
    // Generate buttons for each destination in the toolbar
    toolbarDestinations.forEach { destination ->
        val isSelected = currentRoute == destination.route
        
        IconButton(
            colors = getButtonColors(isSelected),
            onClick = {
                if (!isSelected) {
                    when (destination.route) {
                        Destination.Tracker.route -> {
                            // Navigate to tracker and clear back stack
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                        else -> {
                            // For Analysis and Settings, use launchSingleTop to avoid stacking
                            navController.navigate(destination.route) {
                                popUpTo(Destination.Tracker.route) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }
        ) {
            Icon(
                imageVector = destination.icon,
                contentDescription = destination.label,
                tint = getIconTint(isSelected),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
