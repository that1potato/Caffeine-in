package com.example.caffeine_in.ui.analysis

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.caffeine_in.ui.analysis.components.AnalysisTopBar
import com.example.caffeine_in.ui.analysis.components.PerDaySection
import com.example.caffeine_in.ui.analysis.utils.groupByDay

@Composable
fun AnalysisScreen(
    analysisViewModel: AnalysisViewModel = viewModel(),
    navController: NavController
) {
    val intakeList by analysisViewModel.intakeList.collectAsState()
    //val histogramData = analysisViewModel.getHistogramData()
    
    
    Scaffold(
        containerColor = Color(0xFFECE0D1),
        topBar = {
            AnalysisTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        }
    ) { innerPadding ->
        val modifiedPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            start = 16.dp,
            end = 16.dp,
            bottom = 0.dp
        )
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(modifiedPadding)
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
            /*item {
                Text(
                    text = "Intake History",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38220F),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }*/
            
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
                val groupedIntakes = intakeList.groupByDay()
                
                items(groupedIntakes.size) { index ->
                    val group = groupedIntakes[index]
                    PerDaySection(
                        date = group.dateLabel,
                        intakes = group.intakes
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
