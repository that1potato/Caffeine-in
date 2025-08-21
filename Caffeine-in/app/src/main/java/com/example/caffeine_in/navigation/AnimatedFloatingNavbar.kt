package com.example.caffeine_in.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@ExperimentalMaterial3ExpressiveApi
@Composable
fun AnimatedToolBarFAB(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentRoute: String,
    showFab: Boolean = false,
    onFabClick: () -> Unit = {}
) {
    val toolbarColors = FloatingToolbarColors(
        toolbarContainerColor = Color(0xFFC5B5A6),
        toolbarContentColor = Color(0xFF38220F),
        fabContainerColor = Color(0xFFE57825),
        fabContentColor = Color(0xFF38220F)
    )
    
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navigation buttons in custom toolbar
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = Color(0xFFC5B5A6),
                modifier = Modifier.height(64.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedNavigationButtons(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            }
            
            // Animated FAB
            AnimatedVisibility(
                visible = showFab,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(),
                exit = scaleOut(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeOut()
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = onFabClick,
                    containerColor = Color(0xFFE57825),
                    contentColor = Color(0xFF38220F),
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Caffeine"
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedNavigationButtons(
    navController: NavController,
    currentRoute: String
) {
    toolbarDestinations.forEach { destination ->
        val isSelected = currentRoute == destination.route
        
        val backgroundColor by animateColorAsState(
            targetValue = if (isSelected) Color(0xFF38220F) else Color.Transparent,
            animationSpec = tween(300),
            label = "Background Color"
        )
        
        val iconColor by animateColorAsState(
            targetValue = if (isSelected) Color(0xFFECE0D1) else Color(0xFF38220F),
            animationSpec = tween(300),
            label = "Icon Color"
        )
        
        IconButton(
            onClick = {
                if (!isSelected) {
                    when (destination.route) {
                        Destination.Tracker.route -> {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                        else -> {
                            navController.navigate(destination.route) {
                                popUpTo(Destination.Tracker.route) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            },
            colors = IconButtonColors(
                containerColor = backgroundColor,
                contentColor = iconColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = iconColor
            )
        ) {
            Icon(
                imageVector = destination.icon,
                contentDescription = destination.label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}