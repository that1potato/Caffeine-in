package com.example.caffeine_in.ui.caffeinetracker

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.caffeine_in.caffeineBand.CaffeineBand
import com.example.caffeine_in.data.CaffeineSource
import com.example.caffeine_in.navigation.Destination
import com.example.caffeine_in.ui.caffeinetracker.components.AddNewCaffeineDialog
import com.example.caffeine_in.ui.caffeinetracker.components.EditCaffeineDialog
import com.example.caffeine_in.ui.caffeinetracker.components.History
import com.example.caffeine_in.ui.caffeinetracker.components.HistoryHeader
import com.example.caffeine_in.ui.caffeinetracker.components.IndicatorDialog
import com.example.caffeine_in.ui.caffeinetracker.components.TodaysTotalSection
import com.example.caffeine_in.ui.theme.CaffeineinTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.text.toDouble

const val MAX_CAFFEINE_AMOUNT = 400 // 400mg caffeine intake a day is safe for most adults

@Composable
fun CaffeineTrackerScreen(
    caffeineTrackerViewModel: CaffeineTrackerViewModel = viewModel(),
    navController: NavController,
    paddingValues: PaddingValues = PaddingValues(),
    showAddDialog: Boolean = false,
    onDismissDialog: () -> Unit = {}
) {
    val displayedCaffeineMg by caffeineTrackerViewModel.displayedCaffeineMg
    val historyList by caffeineTrackerViewModel.historyList.collectAsState()
    val showIndicatorDialog = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val shouldScrollToTop by caffeineTrackerViewModel.scrollToTopEvent.collectAsState()
    val totalIntake24Hours by caffeineTrackerViewModel.totalIntake24Hours
    
    // get the current caffeine band to indicate
    val currentBand = CaffeineBand.getBand(totalIntake24Hours.toDouble())
    
    val animatedProgress by animateFloatAsState(
        targetValue = 1.0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "ProgressAnimation"
    )
    
    var isEditMode by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<CaffeineSource?>(null) }
    var snackbarJob: Job? by remember { mutableStateOf(null) }
    var newlyAdded by remember { mutableStateOf(false) }
    
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
    
    // automatically exit edit mode if the last item is deleted
    LaunchedEffect(historyList) {
        if (historyList.isEmpty()) {
            isEditMode = false
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Today's Section ---
            TodaysTotalSection(
                animatedProgress = animatedProgress,
                caffeineAmount = displayedCaffeineMg,
                currentBand = currentBand,
                onInfoClick = { showIndicatorDialog.value = true }
            )
            
            // --- history Section ---
            Column(modifier = Modifier.animateContentSize()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    HistoryHeader(
                        buttonEnabled = historyList.isNotEmpty(),
                        isEditMode = isEditMode,
                        onEditClick = { isEditMode = !isEditMode }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
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
                                        caffeineTrackerViewModel.addCaffeine(amount, source.name)
                                        snackbarJob?.cancel()
                                        snackbarJob = scope.launch {
                                            val result = snackbarHostState.showSnackbar(
                                                message = "${source.name} logged",
                                                actionLabel = "Undo",
                                                duration = SnackbarDuration.Short
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
                                                duration = SnackbarDuration.Short
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
        
        // Snackbar host positioned at bottom
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
                .absoluteOffset(y = (-112).dp)
                .padding(horizontal = 16.dp)
        )
    }
    
    // ---- Dialogs ----
    
    // ---- indicator dialog ----
    if (showIndicatorDialog.value) {
        IndicatorDialog(
            onDismiss = { showIndicatorDialog.value = false },
            onConfirm = { navController.navigate(Destination.Info.route) }
        )
    }
    
    // ---- add dialog ----
    if (showAddDialog) {
        AddNewCaffeineDialog(
            onDismiss = onDismissDialog,
            onConfirm = { name, amount ->
                caffeineTrackerViewModel.addCaffeineSource(name, amount)
                onDismissDialog()
                newlyAdded = true
                snackbarJob?.cancel()
                snackbarJob = scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "$name added and logged",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
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
                    itemToEdit = null
                }
                updated
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun DefaultPreview() {
    CaffeineinTheme {
        CaffeineTrackerScreen(
            navController = rememberNavController(),
            paddingValues = PaddingValues(),
            showAddDialog = false,
            onDismissDialog = {}
        )
    }
}
