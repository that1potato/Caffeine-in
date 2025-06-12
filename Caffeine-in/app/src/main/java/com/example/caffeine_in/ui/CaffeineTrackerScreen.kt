package com.example.caffeine_in.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.setValue


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
        CaffeineSource("Coffee", 95, R.drawable.coffee_placeholder),
        CaffeineSource("Green Tea", 35, R.drawable.green_tea_placeholder),
        CaffeineSource("Energy Drink", 80, R.drawable.energy_drink_placeholder)
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
            .background(Color(0xFFF0F0F0)) // Light gray background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- "Today's" Section ---
                item {
                    TodaysTotalSection()
                    LinearWavyProgressIndicator(
                        progress = { animatedProgress },
                        amplitude = { 1f }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            // --- Main content card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- "Quick Add Suggestions" Section ---
                    item {
                        QuickAddSuggestionsHeader()
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
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "250 mg",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E4A5D) // Dark teal color
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
fun QuickAddSuggestionsHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Quick Add",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )
    }
}


@Composable
fun SuggestionItem(source: CaffeineSource) {
    // --- Card for each suggestion item for a subtle background and shape ---
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)) // Very light gray
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Circular Image ---
            // In a real app, you would load an image from the drawable resources.
            // Here we use a placeholder.
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray) // Placeholder color
            ) {
                // Example of how you would use a real image:
                // Image(
                //     painter = painterResource(id = source.imageRes),
                //     contentDescription = source.name,
                //     contentScale = ContentScale.Crop
                // )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // --- Text Column ---
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = source.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "${source.amount} mg",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            // --- Add Button ---
            Button(
                onClick = { /* Handle specific item add */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E4A5D)),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Text(text = "Add")
            }
        }
    }
}


// --- Preview Function to see the UI in Android Studio ---
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun DefaultPreview() {
    // It's good practice to wrap previews in your app's theme
    // MaterialTheme {
    CaffeineTrackerScreen()
    // }
}

// --- Dummy Drawable Resources for Preview ---
// In a real project, these would be actual image files in res/drawable
object R {
    object drawable {
        const val coffee_placeholder = 1
        const val green_tea_placeholder = 2
        const val energy_drink_placeholder = 3
    }
}