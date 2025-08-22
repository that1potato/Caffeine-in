package com.example.caffeine_in.ui.analysis.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caffeine_in.data.HistogramData
import kotlin.math.max

@Composable
fun CaffeineHistogram(
    data: List<HistogramData>,
    modifier: Modifier = Modifier
) {
    val maxValue = max(data.maxOfOrNull { it.caffeineLevel } ?: 1f, 50f) // Minimum 50mg scale
    val barColor = Color(0xFF967259)
    val textColor = Color(0xFF38220F)

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barWidth = canvasWidth / data.size * 0.7f
            val barSpacing = canvasWidth / data.size * 0.3f

            data.forEachIndexed { index, item ->
                val barHeight = (item.caffeineLevel / maxValue) * canvasHeight
                val barLeft = index * (barWidth + barSpacing) + barSpacing / 2

                drawRect(
                    color = barColor,
                    topLeft = Offset(barLeft, canvasHeight - barHeight),
                    size = Size(barWidth, barHeight)
                )

                // Draw value on top of bar if there's space
                if (barHeight > 30) {
                    drawText(
                        text = "${item.caffeineLevel.toInt()}",
                        x = barLeft + barWidth / 2,
                        y = canvasHeight - barHeight + 20,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Time labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEachIndexed { index, item ->
                if (index % 2 == 0) { // Show every other label to avoid crowding
                    Text(
                        text = item.timeLabel,
                        fontSize = 12.sp,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// Extension function to draw text on Canvas
private fun DrawScope.drawText(
    text: String,
    x: Float,
    y: Float,
    color: Color
) {
    // Note: This is a simplified text drawing. In a real implementation,
    // you might want to use a proper text measurement and drawing approach
    // For now, this serves as a placeholder for the text drawing logic
}
