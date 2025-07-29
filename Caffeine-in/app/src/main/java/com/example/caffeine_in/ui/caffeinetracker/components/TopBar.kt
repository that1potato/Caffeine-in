package com.example.caffeine_in.ui.caffeinetracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TopBar( // TODO
    modifier: Modifier,
    navController: NavController
) {
    val iconColor = Color(0xFF38220F)
    val iconModifier = Modifier.size(24.dp)

    Row(
        modifier = modifier.padding(
            start = 16.dp,
            end = 16.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // ---- analysis ----
        IconButton(onClick = { navController.navigate("analysis") }) {
            Icon(
                imageVector = Icons.Outlined.AutoGraph,
                contentDescription = "Analysis",
                tint = iconColor,
                modifier = iconModifier
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        // ---- settings ----
        IconButton(onClick = { navController.navigate("settings") }) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings",
                tint = iconColor,
                modifier = iconModifier
            )
        }
    }
}
