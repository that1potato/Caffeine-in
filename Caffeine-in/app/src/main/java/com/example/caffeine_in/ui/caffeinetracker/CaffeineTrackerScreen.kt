package com.example.caffeine_in.ui.caffeinetracker

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caffeine_in.ui.theme.CaffeineinTheme
import com.example.caffeine_in.ui.theme.FiraCodeFontFamily
import kotlin.math.roundToInt

const val maxCaffeineAmount = 400 // 400mg caffeine intake a day is safe for most adults

// --- Data class to hold history information ---
data class CaffeineSource(
    val name: String,
    val amount: Int,
)

@Composable
fun CaffeineTrackerScreen(
    caffeineTrackerViewModel: CaffeineTrackerViewModel = viewModel()
) {
    // --- Sample data for the suggestion list ---
    val historyList = listOf(
        CaffeineSource("Coffee", 95),
        CaffeineSource("Green Tea", 35),
        CaffeineSource("Red Bull", 80),
        CaffeineSource("Green Bull", 80),
        CaffeineSource("Yellow Bull", 80),
        CaffeineSource("Blue Bull", 80),
        CaffeineSource("Grey Bull", 80),
        CaffeineSource("Pink Bull", 80)
    )

    // Observe state from the ViewModel
    val displayedCaffeineMg by caffeineTrackerViewModel.displayedCaffeineMg

    val animatedProgress by animateFloatAsState(
        targetValue = 1.0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "ProgressAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECE0D1)) // Light gray background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 0.dp
                )
                .statusBarsPadding(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // --- "Today's" Section ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyColumn(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        TodaysTotalSection(
                            animatedProgress = animatedProgress,
                            caffeineAmount = displayedCaffeineMg
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }

            // --- history card ---
            Column {
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    HistoryHeader()
                }
                LazyColumn(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 0.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(historyList) { source ->
                        History(
                            source = source,
                            onAddCaffeine = { amount ->
                                caffeineTrackerViewModel.addCaffeine(amount)
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(96.dp))
                    }
                }
            }
        }

        // --- Floating add button ---
        FloatingActionButton(
            onClick = { /* TODO: Handle FAB click, e.g., open a dialog to add custom caffeine intake */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp),
            shape = RoundedCornerShape(50.dp),
            containerColor = Color(0xFFE07319)
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
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TodaysTotalSection(animatedProgress: Float, caffeineAmount: Float) {

    val waveSpeed: Float // 30~60
    val waveLength: Float // 30~80

    if (caffeineAmount >= maxCaffeineAmount) {
        waveSpeed = 60f
        waveLength = 30f
    } else if (caffeineAmount == 0f) {
        waveSpeed = 30f
        waveLength = 80f
    } else {
        waveSpeed = EaseInCubic.transform(caffeineAmount/maxCaffeineAmount) * 30 + 30
        waveLength = 80 - EaseInCubic.transform(caffeineAmount/maxCaffeineAmount) * 50
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFECE0D1)),
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Caffeine Level",
                    maxLines = 1,
                    color = Color(0xFF967259),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { /* Handle specific item add */ },
                    modifier = Modifier.size(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF967259)),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Info",
                        tint = Color(0xFFECE0D1)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${caffeineAmount.roundToInt()}mg",
            fontWeight = FontWeight.Bold,
            autoSize = TextAutoSize.StepBased(
                maxFontSize = 80.sp
            ),
            maxLines = 1,
            color = Color(0xFF38220F)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LinearWavyProgressIndicator(
            progress = { animatedProgress },
            amplitude = { 1f },
            waveSpeed = waveSpeed.dp,
            wavelength = waveLength.dp,
            color = Color(0xFF967259),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun HistoryHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "History",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF38220F)
        )
    }
}


@Composable
fun History(
    source: CaffeineSource,
    onAddCaffeine: (Int) -> Unit
) {
    // --- Card for each suggestion item for a subtle background and shape ---
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFECE0D1)),
        border = BorderStroke(width = 1.dp, color = Color(0xFF967259))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Text Column ---
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = source.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF38220F)
                )
                Text(
                    text = "${source.amount}mg",
                    color = Color(0xFF967259),
                    fontSize = 14.sp
                )
            }

            // --- Add Button ---
            Button(
                onClick = { onAddCaffeine(source.amount) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38220F)),
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = Color(0xFFECE0D1)
                )
            }
        }
    }
}


// --- Preview Function to see the UI in Android Studio ---
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun DefaultPreview() {
    CaffeineinTheme {
        CaffeineTrackerScreen()
    }
}
