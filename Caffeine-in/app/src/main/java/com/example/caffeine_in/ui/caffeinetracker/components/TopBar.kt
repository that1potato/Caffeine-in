package com.example.caffeine_in.ui.caffeinetracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
    val buttonColor = ButtonDefaults.buttonColors(containerColor = Color(0xFFECE0D1))
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
        Button(
            onClick = { /*TODO*/ },
            colors = buttonColor,
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoGraph,
                contentDescription = "Log",
                tint = iconColor,
                modifier = iconModifier
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        // ---- settings ----
        Button(
            onClick = { navController.navigate("settings") },
            colors = buttonColor,
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Log",
                tint = iconColor,
                modifier = iconModifier
            )
        }
    }
}
