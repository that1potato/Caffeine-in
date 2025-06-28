package com.example.caffeine_in.ui.caffeinetracker.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caffeine_in.data.CaffeineSource

@Composable
fun HistoryHeader(
    buttonEnabled: Boolean,
    isEditMode: Boolean,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Caffeine Sources",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF38220F)
        )
        Spacer(Modifier.weight(1f))
        // edit button
        Button(
            onClick = onEditClick,
            enabled = buttonEnabled,
            modifier = Modifier.size(30.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECE0D1)),
            contentPadding = PaddingValues(0.dp)
        ) {
            AnimatedContent(
                targetState = isEditMode,
                transitionSpec = {
                    (scaleIn() + fadeIn()) togetherWith (scaleOut() + fadeOut())
                },
                label = "EditButtonIconAnimation"
            ) { targetState ->
                Icon(
                    imageVector = if (targetState) Icons.Filled.Check else Icons.Filled.Edit,
                    contentDescription = if (targetState) "Done" else "Edit",
                    tint = if (buttonEnabled) Color(0xFF38220F) else Color(0xFF967259)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun History(
    source: CaffeineSource,
    isEditMode: Boolean,
    onAddCaffeine: (Int) -> Unit,
    onDeleteSource: (CaffeineSource) -> Unit,
    onEditClick: (CaffeineSource) -> Unit
) {
    val buttonColor by animateColorAsState(
        targetValue = if (isEditMode) Color(0xFFE53935) else Color(0xFF38220F),
        label = "HistoryButtonColorAnimation"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = isEditMode, // clickable in edit mode
                onClick = { onEditClick(source) }
            ),
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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF38220F)
                )
                Text(
                    text = "${source.amount}mg",
                    color = Color(0xFF967259),
                    fontSize = 14.sp
                )
            }
            
            // --- Add/Delete Button ---
            Button(
                onClick = {
                    if (isEditMode) {
                        onDeleteSource(source)
                    } else {
                        onAddCaffeine(source.amount)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor // Use the animated color here
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                AnimatedContent(
                    targetState = isEditMode,
                    transitionSpec = {
                        (scaleIn() + fadeIn()) togetherWith (scaleOut() + fadeOut())
                    },
                    label = "HistoryButtonIconAnimation"
                ) { targetState ->
                    if (targetState) {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = "Delete",
                            tint = Color(0xFFECE0D1)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = Color(0xFFECE0D1)
                        )
                    }
                }
            }
        }
    }
}
