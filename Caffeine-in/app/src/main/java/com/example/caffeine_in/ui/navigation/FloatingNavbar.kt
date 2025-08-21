package com.example.caffeine_in.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Settings
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
    onFabClick: () -> Unit,
    expanded: Boolean = true
) {
    val toolbarColors = FloatingToolbarColors(
        toolbarContainerColor = Color(0xFFC5B5A6),
        toolbarContentColor = Color(0xFF38220F),
        fabContainerColor = Color(0xFFE57825),
        fabContentColor = Color(0xFF38220F)
    )
    
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
            // Analysis button
            IconButton(
                onClick = { navController.navigate("analysis") }
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoGraph,
                    contentDescription = "Analysis",
                    tint = Color(0xFF38220F),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Tracker button
            IconButton(
                colors = IconButtonColors(
                    containerColor = Color(0xFF38220F),
                    contentColor = Color(0xFF38220F),
                    disabledContainerColor = Color(0xFFC5B5A6),
                    disabledContentColor = Color(0xFF38220F)
                ),
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Coffee,
                    contentDescription = "Tracker",
                    tint = Color(0xFFECE0D1),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Settings button
            IconButton(
                onClick = { navController.navigate("settings") }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = Color(0xFF38220F),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}