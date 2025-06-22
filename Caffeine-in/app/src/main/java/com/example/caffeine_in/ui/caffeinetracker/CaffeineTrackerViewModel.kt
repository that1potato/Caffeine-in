package com.example.caffeine_in.ui.caffeinetracker

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.caffeine_in.data.CaffeineSource
import com.example.caffeine_in.data.DataRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.pow

const val CAFFEINE_HALF_LIFE_HOURS_VM = 5.0 // caffeine's half life is about 5hrs on average
const val CAFFEINE_HALF_LIFE_MILLIS_VM = CAFFEINE_HALF_LIFE_HOURS_VM * 60 * 60 * 1000
const val DELAY_INTERVAL = 2000L // 2sec

class CaffeineTrackerViewModel(application: Application) : AndroidViewModel(application) {

    private val dataRepository = DataRepository(application)

    // val initialCaffeineMg: State<Float> = _initialCaffeineMg
    private val _initialCaffeineMg = mutableFloatStateOf(0f)

    // val lastIngestionTimeMillis: State<Long> = _lastIngestionTimeMillis
    private val _lastIngestionTimeMillis = mutableLongStateOf(0L)

    private val _displayedCaffeineMg = mutableFloatStateOf(0f)
    val displayedCaffeineMg: State<Float> = _displayedCaffeineMg // Expose as immutable State to the UI

    private var decayCalculationJob: Job? = null // manage the decay coroutine

    // StateFlow for the History List
    private val _historyList = MutableStateFlow<List<CaffeineSource>>(emptyList())
    val historyList: StateFlow<List<CaffeineSource>> = _historyList.asStateFlow()

    init {
        // preload showcase data if empty && first time launching
        viewModelScope.launch {
            val hasBeenLaunchedBefore = dataRepository.hasBeenLaunchedBeforeFlow.first()
            val isHistoryEmpty = dataRepository.historyListFlow.first().isEmpty()
            
            // Check if the app has been launched before.
            if (!hasBeenLaunchedBefore && isHistoryEmpty) {
                val initialList = listOf(
                    CaffeineSource("1 Espresso Shot", 64),
                    CaffeineSource("Green Tea", 35),
                    CaffeineSource("Red Bull", 80),
                    CaffeineSource("Grey Bull", 80),
                    CaffeineSource("Pink Bull", 80)
                )
                
                dataRepository.setHistoryList(initialList)
                dataRepository.setHasBeenLaunchedBefore()
            }
        }

        loadAndStart()
        viewModelScope.launch {
            dataRepository.historyListFlow.collect { history ->
                _historyList.value = history
            }
        }
    }

    private fun loadAndStart() {
        viewModelScope.launch {
            // Load saved state
            val (savedInitialMg, savedLastIngestionTime) = dataRepository.caffeineStateFlow.first()

            if (savedInitialMg > 0f && savedLastIngestionTime > 0L) {
                _initialCaffeineMg.floatValue = savedInitialMg
                _lastIngestionTimeMillis.longValue = savedLastIngestionTime
                // The decay calculation will correctly set _displayedCaffeineMg
            } else {
                // If nothing is saved or values are invalid, ensure a clean state
                _initialCaffeineMg.floatValue = 0f
                _lastIngestionTimeMillis.longValue = 0L
                _displayedCaffeineMg.floatValue = 0f
            }
            startOrRestartDecayCalculation() // Start decay calculation after loading
        }
    }

    private fun startOrRestartDecayCalculation() {
        decayCalculationJob?.cancel()
        decayCalculationJob = viewModelScope.launch {
            if (_initialCaffeineMg.floatValue == 0f || _lastIngestionTimeMillis.longValue == 0L) {
                _displayedCaffeineMg.floatValue = 0f
                dataRepository.clearCaffeineState() // clear persisted
                return@launch
            }

            while (true) {
                if (_initialCaffeineMg.floatValue == 0f) {
                    _displayedCaffeineMg.floatValue = 0f
                    dataRepository.clearCaffeineState() // clear persisted state if it decays to zero
                    break
                }

                val currentTimeMillis = System.currentTimeMillis()
                val timeElapsedMillis = (currentTimeMillis - _lastIngestionTimeMillis.longValue).toDouble()

                // If lastIngestionTime is in the future (e.g., device time changed),
                // treat it as no time elapsed for decay calculation.
                // Or, more robustly, cap timeElapsedMillis at 0 if negative.
                val effectiveTimeElapsedMillis = if (timeElapsedMillis < 0) 0.0 else timeElapsedMillis


                // Calculate based on the initially ingested amount and the total time elapsed since that ingestion
                val currentCalculatedMg = _initialCaffeineMg.floatValue * (0.5).pow(effectiveTimeElapsedMillis / CAFFEINE_HALF_LIFE_MILLIS_VM)
                _displayedCaffeineMg.floatValue = if (currentCalculatedMg < 1) 0f else currentCalculatedMg.toFloat()

                if (_displayedCaffeineMg.floatValue == 0f) {
                    //reset internal state and persisted state.
                    _initialCaffeineMg.floatValue = 0f
                    _lastIngestionTimeMillis.longValue = 0L // Reset this too
                    dataRepository.clearCaffeineState() // Clear from DataStore
                }
                delay(DELAY_INTERVAL)
            }
        }
    }

    fun addCaffeine(amount: Int) {
        if (amount <= 0) return

        viewModelScope.launch {
            val currentTimeMillis = System.currentTimeMillis()
            var currentEffectiveMg = 0.0

            // If there was previous caffeine, calculate its current decayed value
            if (_initialCaffeineMg.floatValue > 0f && _lastIngestionTimeMillis.longValue > 0L) {
                val timeElapsedSinceLastIngestionMillis = (currentTimeMillis - _lastIngestionTimeMillis.longValue).toDouble()
                currentEffectiveMg = if (timeElapsedSinceLastIngestionMillis > 0) {
                    _initialCaffeineMg.floatValue * (0.5).pow(timeElapsedSinceLastIngestionMillis / CAFFEINE_HALF_LIFE_MILLIS_VM)
                } else {
                    // If no time has passed or time is somehow negative, use the last known initial amount.
                    _initialCaffeineMg.floatValue.toDouble()
                }
            }
            // Ensure currentEffectiveMg isn't negative or extremely small before adding
            if (currentEffectiveMg < 0.5) currentEffectiveMg = 0.0


            val newTotalInitialEquivalentMg = (currentEffectiveMg + amount).toFloat()

            _initialCaffeineMg.floatValue = newTotalInitialEquivalentMg
            _lastIngestionTimeMillis.longValue = currentTimeMillis // This is the new "start" point for this total amount
            _displayedCaffeineMg.floatValue = newTotalInitialEquivalentMg // Update display immediately

            // Save the new state
            dataRepository.saveCaffeineState(newTotalInitialEquivalentMg, currentTimeMillis)
            startOrRestartDecayCalculation()
        }
    }

    fun resetCaffeineTracker() {
        viewModelScope.launch {
            _initialCaffeineMg.floatValue = 0f
            _lastIngestionTimeMillis.longValue = 0L
            _displayedCaffeineMg.floatValue = 0f
            dataRepository.clearCaffeineState()
            decayCalculationJob?.cancel() // Stop any ongoing calculation
        }
    }

    fun addCaffeineSource(name: String, amount: Int) {
        if (name.isBlank() || amount <= 0) return
        viewModelScope.launch {
            val newSource = CaffeineSource(name, amount)
            dataRepository.addHistoryItem(newSource)
            addCaffeine(amount)
        }
    }
    
    suspend fun updateCaffeineSource(oldSource: CaffeineSource, newName: String, newAmount: Int): Boolean {
        if (newName.isBlank() || newAmount <= 0) return false
        return dataRepository.updateHistoryItem(oldSource, newName, newAmount)
    }

    fun removeCaffeineSource(source: CaffeineSource) {
        viewModelScope.launch {
            dataRepository.removeHistoryItem(source)
        }
    }

    override fun onCleared() {
        super.onCleared()
        decayCalculationJob?.cancel()
    }
}