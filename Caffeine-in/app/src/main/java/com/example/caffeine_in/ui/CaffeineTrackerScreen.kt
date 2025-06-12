package com.example.caffeine_in.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text


// Note: You would need to add actual images to your `res/drawable` folder
// for this to work. I've used placeholder names like `R.drawable.coffee`.
// If you don't have these, the app will crash. For preview purposes,
// you can replace the Image composable with a colored Box.

// --- Data class to hold suggestion information ---
data class CaffeineSource(
    val name: String,
    val amount: Int,
    val imageRes: Int // Using Int for drawable resource ID
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CaffeineTrackerScreen() {
    // --- Sample data for the suggestion list ---
    val suggestions = listOf(
        CaffeineSource("Coffee", 95, R.Drawable.coffee_placeholder),
        CaffeineSource("Green Tea", 35, R.Drawable.green_tea_placeholder),
        CaffeineSource("Energy Drink", 80, R.Drawable.energy_drink_placeholder)
    )

    val animatedProgress by animateFloatAsState(
        targetValue = 1.0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "ProgressAnimation"
    )

    // --- Main container with a light gray background ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECE0D1)) // Light gray background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                        TodaysTotalSection()
                        LinearWavyProgressIndicator(
                            progress = { animatedProgress },
                            amplitude = { 1f },
                            color = Color(0xFF967259)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }

            // --- quick add card ---
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- "Quick Add Suggestions" Section ---
                item {
                    QuickAddHeader()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // --- List of suggestion items ---
                items(suggestions) { source ->
                    SuggestionItem(source = source)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun TodaysTotalSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Today's",
            fontSize = 22.sp,
            color = Color(0xFF967259)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "250 mg",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF38220F)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@ExperimentalMaterial3ExpressiveApi
@Composable
fun LinearWavyProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = WavyProgressIndicatorDefaults.indicatorColor,
    trackColor: Color = WavyProgressIndicatorDefaults.trackColor,
    stroke: Stroke = WavyProgressIndicatorDefaults.linearIndicatorStroke,
    trackStroke: Stroke = WavyProgressIndicatorDefaults.linearTrackStroke,
    gapSize: Dp = WavyProgressIndicatorDefaults.LinearIndicatorTrackGapSize,
    stopSize: Dp = WavyProgressIndicatorDefaults.LinearTrackStopIndicatorSize,
    amplitude: (progress: Float) -> Float = WavyProgressIndicatorDefaults.indicatorAmplitude,
    wavelength: Dp = WavyProgressIndicatorDefaults.LinearDeterminateWavelength,
) {}

@Composable
fun QuickAddHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Quick Add",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF38220F)
        )
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SuggestionItem(source: CaffeineSource) {
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
                    text = "${source.amount} mg",
                    color = Color(0xFF967259),
                    fontSize = 14.sp
                )
            }

            // --- Add Button ---
            Button(
                onClick = { /* Handle specific item add */ },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38220F)),
                contentPadding = PaddingValues(horizontal = 24.dp)
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
    MaterialTheme {
        CaffeineTrackerScreen()
    }
}

// --- Dummy Drawable Resources for Preview ---
// In a real project, these would be actual image files in res/drawable
object R {
    object Drawable {
        const val coffee_placeholder = 1
        const val green_tea_placeholder = 2
        const val energy_drink_placeholder = 3
    }
}