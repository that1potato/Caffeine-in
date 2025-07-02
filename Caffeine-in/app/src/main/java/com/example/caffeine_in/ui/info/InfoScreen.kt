package com.example.caffeine_in.ui.info

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InfoScreen(navController: NavController) {
    Scaffold(
        containerColor = Color(0xFFECE0D1),
        topBar = {
            InfoTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                navController = navController
            )
        }
    ) { innerPadding ->
        val modifiedPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
            bottom = 0.dp
        )
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(modifiedPadding)
                .padding(horizontal = 32.dp)
        ) {
            item { }
            item {
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTopBar(
    modifier: Modifier,
    navController: NavController
) {
    val textIconColor = Color(0xFF38220F)
    val iconModifier = Modifier.size(24.dp)
    
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(Color(0xFFECE0D1)),
        title = {
            Text(
                text = "Info",
                fontWeight = FontWeight.Bold,
                color = textIconColor
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(
                    modifier = iconModifier,
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = textIconColor
                )
            }
        }
    )
}
