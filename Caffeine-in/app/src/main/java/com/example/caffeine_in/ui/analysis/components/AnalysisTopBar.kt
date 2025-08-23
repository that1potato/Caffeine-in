package com.example.caffeine_in.ui.analysis.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisTopBar(
    modifier: Modifier,
) {
    val textIconColor = Color(0xFF38220F)
    
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(Color(0xFFECE0D1)),
        title = {
            Text(
                text = "Analysis",
                fontWeight = FontWeight.Bold,
                color = textIconColor
            )
        }
    )
}
