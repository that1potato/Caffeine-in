package com.example.caffeine_in.ui.caffeinetracker.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caffeine_in.data.CaffeineSource
import kotlinx.coroutines.launch

@Composable
fun AddNewCaffeineDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, amount: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF38220F),
        unfocusedTextColor = Color(0xFF38220F),
        focusedContainerColor = Color(0xFFECE0D1),
        unfocusedBorderColor = Color(0xFF967259),
        focusedBorderColor = Color(0xFF967259),
        unfocusedLabelColor = Color(0xFF967259),
        focusedLabelColor = Color(0xFF38220F),
    )
    
    AlertDialog(
        containerColor = Color(0xFFECE0D1),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "New Caffeine Source",
                color = Color(0xFF38220F),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name (e.g. Espresso Shot)") },
                    singleLine = true,
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it.filter { c -> c.isDigit() } },
                    label = { Text("Caffeine (mg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = textFieldColors
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountInt = amount.toIntOrNull()
                    if (name.isNotBlank() && amountInt != null && amountInt > 0) {
                        onConfirm(name, amountInt)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38220F))
            ) {
                Text(
                    text = "Add",
                    color = Color(0xFFECE0D1)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = Color(0xFF38220F)
                )
            }
        }
    )
}

@Composable
fun EditCaffeineDialog(
    item: CaffeineSource,
    onDismiss: () -> Unit,
    onConfirm: suspend (newName: String, newAmount: Int) -> Boolean // Returns true on success
) {
    var name by remember { mutableStateOf(item.name) }
    var amount by remember { mutableStateOf(item.amount.toString()) }
    var showError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF38220F),
        unfocusedTextColor = Color(0xFF38220F),
        focusedContainerColor = Color(0xFFECE0D1),
        unfocusedBorderColor = Color(0xFF967259),
        focusedBorderColor = Color(0xFF967259),
        unfocusedLabelColor = Color(0xFF967259),
        focusedLabelColor = Color(0xFF38220F),
        errorBorderColor = Color.Red,
        errorLabelColor = Color.Red
    )
    
    AlertDialog(
        containerColor = Color(0xFFECE0D1),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Caffeine Source",
                color = Color(0xFF38220F),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                if (showError) {
                    Text("An item with this name already exists.", color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        showError = false // Hide error on new input
                    },
                    label = { Text("Name") },
                    singleLine = true,
                    colors = textFieldColors,
                    isError = showError
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it.filter { c -> c.isDigit() }
                        showError = false
                    },
                    label = { Text("Caffeine (mg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = textFieldColors
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountInt = amount.toIntOrNull()
                    if (name.isNotBlank() && amountInt != null && amountInt > 0) {
                        scope.launch {
                            val success = onConfirm(name, amountInt)
                            if (!success) {
                                showError = true
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38220F))
            ) {
                Text("Save", color = Color(0xFFECE0D1))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF38220F))
            }
        }
    )
}

@Composable
fun IndicatorDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        containerColor = Color(0xFFECE0D1),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "New Caffeine Source",
                color = Color(0xFF38220F),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
        
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38220F))
            ) {
                Text(
                    text = "Learn More",
                    color = Color(0xFFECE0D1)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Dismiss",
                    color = Color(0xFF38220F)
                )
            }
        }
    )
}
