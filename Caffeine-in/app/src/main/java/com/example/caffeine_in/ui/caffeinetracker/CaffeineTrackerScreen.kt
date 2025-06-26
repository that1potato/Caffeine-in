package com.example.caffeine_in.ui.caffeinetracker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caffeine_in.data.CaffeineSource
import com.example.caffeine_in.ui.theme.CaffeineinTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

const val MAX_CAFFEINE_AMOUNT = 400 // 400mg caffeine intake a day is safe for most adults

@Composable
fun CaffeineTrackerScreen(
    caffeineTrackerViewModel: CaffeineTrackerViewModel = viewModel()
) {
    val displayedCaffeineMg by caffeineTrackerViewModel.displayedCaffeineMg
    val historyList by caffeineTrackerViewModel.historyList.collectAsState()
    val showAddDialog = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val shouldScrollToTop by caffeineTrackerViewModel.scrollToTopEvent.collectAsState()
    
    val animatedProgress by animateFloatAsState(
        targetValue = 1.0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "ProgressAnimation"
    )
    
    var isEditMode by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<CaffeineSource?>(null) }
    var snackbarJob: Job? by remember { mutableStateOf(null) }
    var newlyAdded by remember { mutableStateOf(false) } // when an item was just added
    
    // scroll up if new item added
    LaunchedEffect(historyList) {
        if (newlyAdded) {
            listState.animateScrollToItem(0)
            newlyAdded = false
        }
    }
    
    // also scroll up if undo index 0 deletion
    LaunchedEffect(shouldScrollToTop) {
        if (shouldScrollToTop) {
            listState.animateScrollToItem(0)
            caffeineTrackerViewModel.onScrollToTopEventConsumed()
        }
    }
    
    LaunchedEffect(historyList) {
        // automatically exit edit mode if the last item is deleted
        if (historyList.isEmpty()) {
            isEditMode = false
        }
    }
    
    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color(0xFFECE0D1),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            NewSourceFAB(
                onClick = { showAddDialog.value = true }
            )
        }
    ) { innerPadding ->
        val modifiedPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
            bottom = 0.dp
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(modifiedPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        top = 0.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 0.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    // --- Today's Section ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TodaysTotalSection(
                            animatedProgress = animatedProgress,
                            caffeineAmount = displayedCaffeineMg
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    
                    // --- history Section ---
                    Column(modifier = Modifier.animateContentSize()) {
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            HistoryHeader(
                                buttonEnabled = historyList.isNotEmpty(),
                                isEditMode = isEditMode,
                                onEditClick = { isEditMode = !isEditMode }
                            )
                        }
                        LazyColumn(
                            modifier = Modifier.padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 0.dp
                            ),
                            state = listState,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (historyList.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .height(48.dp)
                                            .animateItem(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Add your first caffeine source to get started.",
                                            color = Color(0xFF967259),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            } else {
                                items(
                                    historyList,
                                    key = { it.name }
                                ) { source ->
                                    Column(
                                        modifier = Modifier.animateItem()
                                    ) {
                                        History(
                                            source = source,
                                            isEditMode = isEditMode,
                                            onAddCaffeine = { amount ->
                                                caffeineTrackerViewModel.addCaffeine(amount)
                                                snackbarJob?.cancel()
                                                snackbarJob = scope.launch {
                                                    val result = snackbarHostState.showSnackbar(
                                                        message = "${source.name} logged",
                                                        actionLabel = "Undo",
                                                        duration = SnackbarDuration.Long
                                                    )
                                                    if (result == SnackbarResult.ActionPerformed) {
                                                        caffeineTrackerViewModel.undoLastCaffeineAddition()
                                                    }
                                                }
                                            },
                                            onDeleteSource = { sourceToDelete ->
                                                caffeineTrackerViewModel.removeCaffeineSource(sourceToDelete)
                                                snackbarJob?.cancel()
                                                snackbarJob = scope.launch {
                                                    val result = snackbarHostState.showSnackbar(
                                                        message = "${sourceToDelete.name} removed",
                                                        actionLabel = "Undo",
                                                        duration = SnackbarDuration.Long
                                                    )
                                                    if (result == SnackbarResult.ActionPerformed) {
                                                        caffeineTrackerViewModel.undoDeleteCaffeineSource()
                                                    }
                                                }
                                            },
                                            onEditClick = { item ->
                                                itemToEdit = item
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(96.dp))
                            }
                        }
                    }
                }
            }
            
            // ---- add dialog ----
            if (showAddDialog.value) {
                AddNewCaffeineDialog(
                    onDismiss = { showAddDialog.value = false },
                    onConfirm = { name, amount ->
                        caffeineTrackerViewModel.addCaffeineSource(name, amount)
                        showAddDialog.value = false
                        newlyAdded = true
                        snackbarJob?.cancel()
                        snackbarJob = scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "$name added and logged",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Long
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                caffeineTrackerViewModel.removeCaffeineSource(CaffeineSource(name, amount))
                                caffeineTrackerViewModel.undoLastCaffeineAddition()
                            }
                        }
                    }
                )
            }
            
            // ---- edit dialog ----
            itemToEdit?.let { currentItem ->
                EditCaffeineDialog(
                    item = currentItem,
                    onDismiss = { itemToEdit = null },
                    onConfirm = { newName, newAmount ->
                        val updated = caffeineTrackerViewModel.updateCaffeineSource(
                            oldSource = currentItem,
                            newName = newName,
                            newAmount = newAmount
                        )
                        if (updated) {
                            itemToEdit = null // Dismiss dialog on success
                        }
                        updated
                    }
                )
            }
        }
    }
}

@Composable
fun TopBar( // TODO
    modifier: Modifier
) {
    val buttonColor = ButtonDefaults.buttonColors(containerColor = Color(0xFFECE0D1))
    val iconColor = Color(0xFF38220F)
    val iconModifier = Modifier.size(24.dp)
    
    Row(
        modifier = modifier.padding(
            start = 16.dp,
            end = 16.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { /*TODO*/ },
            colors = buttonColor,
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoGraph,
                contentDescription = "Log",
                tint = iconColor,
                modifier = iconModifier
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { /*TODO*/ },
            colors = buttonColor,
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Log",
                tint = iconColor,
                modifier = iconModifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TodaysTotalSection(animatedProgress: Float, caffeineAmount: Float) {

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
            onClick = { /* TODO */ },
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

@Composable
fun NewSourceFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        containerColor = Color(0xFFE57825)
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

/*@Composable
fun TransparentNewSourceFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {}*/

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


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun DefaultPreview() {
    CaffeineinTheme {
        CaffeineTrackerScreen()
    }
}
