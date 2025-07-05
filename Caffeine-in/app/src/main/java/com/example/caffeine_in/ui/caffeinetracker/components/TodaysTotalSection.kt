package com.example.caffeine_in.ui.caffeinetracker.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caffeine_in.ui.caffeinetracker.MAX_CAFFEINE_AMOUNT
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TodaysTotalSection(
    animatedProgress: Float,
    caffeineAmount: Float,
    onInfoClick: () -> Unit
) {
    
    // desired waveSpeed and waveLength based on caffeineAmount
    val targetWaveSpeed: Float
    val targetWaveLength: Float
    
    if (caffeineAmount >= MAX_CAFFEINE_AMOUNT) {
        targetWaveSpeed = 60f
        targetWaveLength = 30f
    } else if (caffeineAmount == 0f) {
        targetWaveSpeed = 30f
        targetWaveLength = 80f
    } else {
        targetWaveSpeed = EaseInCubic.transform(caffeineAmount / MAX_CAFFEINE_AMOUNT) * 30 + 30
        targetWaveLength = 80 - EaseInCubic.transform(caffeineAmount / MAX_CAFFEINE_AMOUNT) * 50
    }
    
    // initialize with the initial calculated values
    var currentWaveSpeedForAnimation by remember { mutableFloatStateOf(targetWaveSpeed) }
    var currentWaveLengthForAnimation by remember { mutableFloatStateOf(targetWaveLength) }
    
    // if adds
    LaunchedEffect(targetWaveSpeed) {
        if (targetWaveSpeed > currentWaveSpeedForAnimation) {
            currentWaveSpeedForAnimation = targetWaveSpeed
        }
    }
    
    LaunchedEffect(targetWaveLength) {
        if (targetWaveLength < currentWaveLengthForAnimation) {
            currentWaveLengthForAnimation = targetWaveLength
        }
    }
    
    val animatedWaveSpeed by animateFloatAsState(
        targetValue = currentWaveSpeedForAnimation,
        animationSpec = tween(
            //durationMillis = 450,
            easing = LinearEasing
        ),
        label = "WaveSpeedAnimation"
    )
    
    val animatedWaveLength by animateFloatAsState(
        targetValue = currentWaveLengthForAnimation,
        animationSpec = tween(
            //durationMillis = 450,
            easing = LinearEasing
        ),
        label = "WaveLengthAnimation"
    )
    
    /*val targetFontSize = when {
        caffeineAmount.roundToInt() < 1000 -> 80.sp
        caffeineAmount.roundToInt() < 10000 -> 78.sp
        caffeineAmount.roundToInt() < 100000 -> 75.sp
        else -> 72.sp
    }*/
    
    /*val animatedFontSize by animateDpAsState(
        targetValue = targetFontSize.value.dp, // Use the targetFontSize determined above
        label = "FontSizeAnimation",
        animationSpec = tween(durationMillis = 300)
    )*/
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // ---- info ----
        Button(
            onClick = onInfoClick,
            //modifier = Modifier.size(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECE0D1)),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Caffeine Level",
                    maxLines = 1,
                    color = Color(0xFF967259),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    tint = Color(0xFF967259),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            AnimatedContent(
                targetState = caffeineAmount.roundToInt(),
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { height -> height } + fadeIn() togetherWith
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        slideInVertically { height -> -height } + fadeIn() togetherWith
                                slideOutVertically { height -> height } + fadeOut()
                    }
                },
                label = "CaffeineAmountNumberAnimation"
            ) { targetCaffeineAmount ->
                Text(
                    text = "$targetCaffeineAmount",
                    fontWeight = FontWeight.Bold,
                    fontSize = 80.sp,
                    maxLines = 1,
                    color = Color(0xFF38220F)
                )
            }
            Text( // Static "mg" unit
                text = "mg",
                fontWeight = FontWeight.Bold,
                fontSize = 80.sp,
                maxLines = 1,
                color = Color(0xFF38220F),
                modifier = Modifier.padding(start = 4.dp) // Add a small space
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearWavyProgressIndicator(
            progress = { animatedProgress },
            amplitude = { 1f },
            waveSpeed = animatedWaveSpeed.dp,
            wavelength = animatedWaveLength.dp,
            color = Color(0xFF967259),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}
