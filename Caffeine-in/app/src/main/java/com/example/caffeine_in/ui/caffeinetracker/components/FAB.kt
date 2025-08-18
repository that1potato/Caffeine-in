package com.example.caffeine_in.ui.caffeinetracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingToolbarHorizontalFabPosition
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NewSourceFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        containerColor = Color(0xFFE57825)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Caffeine",
                tint = Color(0xFF38220F)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "New Caffeine Source",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF38220F)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ToolBarFAB(
    navController: NavController,
    currentDestination: String?,
    onFabClick: () -> Unit
) {
    val items = listOf(
        NavItem("analysis", Icons.Outlined.AutoGraph, "Analysis"),
        NavItem("tracker", Icons.Outlined.Coffee, "Tracker"),
        NavItem("settings", Icons.Outlined.Settings, "Settings")
    )

    HorizontalFloatingToolbar(
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
        floatingActionButtonPosition = FloatingToolbarHorizontalFabPosition.End
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFECE0D1),
                    unselectedIconColor = Color(0xFF38220F),
                    indicatorColor = Color(0xFF38220F)
                )
            )
        }
    }
}

private data class NavItem(val route: String, val icon: ImageVector, val label: String)
