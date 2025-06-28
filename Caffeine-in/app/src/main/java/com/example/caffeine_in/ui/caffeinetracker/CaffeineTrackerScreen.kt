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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caffeine_in.data.CaffeineSource
import com.example.caffeine_in.ui.caffeinetracker.components.AddNewCaffeineDialog
import com.example.caffeine_in.ui.caffeinetracker.components.EditCaffeineDialog
import com.example.caffeine_in.ui.caffeinetracker.components.History
import com.example.caffeine_in.ui.caffeinetracker.components.HistoryHeader
import com.example.caffeine_in.ui.caffeinetracker.components.NewSourceFAB
import com.example.caffeine_in.ui.caffeinetracker.components.TodaysTotalSection
import com.example.caffeine_in.ui.caffeinetracker.components.TopBar
import com.example.caffeine_in.ui.theme.CaffeineinTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
    
    // automatically exit edit mode if the last item is deleted
    LaunchedEffect(historyList) {
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

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun DefaultPreview() {
    CaffeineinTheme {
        CaffeineTrackerScreen()
    }
}
