package com.example.caffeine_in.ui.info

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.caffeine_in.ui.info.components.CaffeineStudy
import com.example.caffeine_in.ui.info.components.Indicator
import com.example.caffeine_in.ui.info.components.InfoTopBar

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
                .padding(horizontal = 16.dp)
        ) {
            item {
                Indicator()
            }
            item {
                CaffeineStudy()
            }
            item {
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
