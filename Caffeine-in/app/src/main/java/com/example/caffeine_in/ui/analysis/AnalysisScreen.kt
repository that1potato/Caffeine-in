package com.example.caffeine_in.ui.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun AnalysisScreen(
    analysisViewModel: AnalysisViewModel = viewModel(),
    navController: NavController
) {
    val intakeList by analysisViewModel.intakeList.collectAsState()
    val histogramData = analysisViewModel.getHistogramData()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AnalysisTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            navController = navController
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            // histogram
            /*item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Caffeine Level (Past 24 Hours)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38220F),
                )
                Spacer(modifier = Modifier.height(16.dp))
                CaffeineHistogram(
                    data = histogramData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
            }*/

            // intake history
            item {
                Text(
                    text = "Intake History",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38220F),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (intakeList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No caffeine intake recorded yet",
                            color = Color(0xFF967259),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(intakeList.size) { index ->
                    IntakeHistoryItem(
                        intake = intakeList[index]
                    )
                    if (index < intakeList.lastIndex) {
                        HorizontalDivider(color = Color(0x5F967259))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
