package com.example.caffeine_in.ui.caffeinetracker

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow

const val CAFFEINE_HALF_LIFE_HOURS_VM = 5.0 // caffeine's half life is about 5hrs on average
const val CAFFEINE_HALF_LIFE_MILLIS_VM = CAFFEINE_HALF_LIFE_HOURS_VM * 60 * 60 * 1000

class CaffeineTrackerViewModel : ViewModel() {

    // val initialCaffeineMg: State<Float> = _initialCaffeineMg
    private val _initialCaffeineMg = mutableFloatStateOf(0f)

    // val lastIngestionTimeMillis: State<Long> = _lastIngestionTimeMillis
    private val _lastIngestionTimeMillis = mutableLongStateOf(0L)

    private val _displayedCaffeineMg = mutableFloatStateOf(0f)
    val displayedCaffeineMg: State<Float> = _displayedCaffeineMg // Expose as immutable State to the UI

    private var decayCalculationJob: Job? = null // manage the decay coroutine

    init {
        val simulatedInitialAmount = 300 // Example: could be 0 or loaded
        if (simulatedInitialAmount > 0) {
            _initialCaffeineMg.floatValue = simulatedInitialAmount.toFloat()
            _lastIngestionTimeMillis.longValue = System.currentTimeMillis()
            _displayedCaffeineMg.floatValue = simulatedInitialAmount.toFloat()
        }
        startOrRestartDecayCalculation() // Start decay calculation
    }

    private fun startOrRestartDecayCalculation() {
        decayCalculationJob?.cancel() // Cancel any existing job
        decayCalculationJob = viewModelScope.launch {
            if (_initialCaffeineMg.floatValue == 0f) {
                _displayedCaffeineMg.floatValue = 0f
                return@launch // Don't start if no initial caffeine
            }

            while (true) {
                // Exit condition if caffeine is no longer being tracked (e.g. reset)
                if (_initialCaffeineMg.floatValue == 0f) {
                    _displayedCaffeineMg.floatValue = 0f
                    break
                }

                val currentTimeMillis = System.currentTimeMillis()
                val timeElapsedMillis = (currentTimeMillis - _lastIngestionTimeMillis.longValue).toDouble()

                if (timeElapsedMillis < 0) { // Should ideally not happen if time is set correctly
                    _displayedCaffeineMg.floatValue = _initialCaffeineMg.floatValue
                    delay(1000)
                    continue
                }

                val currentCalculatedMg = _initialCaffeineMg.floatValue * (0.5).pow(timeElapsedMillis / CAFFEINE_HALF_LIFE_MILLIS_VM)
                _displayedCaffeineMg.floatValue = if (currentCalculatedMg < 1) 0f else currentCalculatedMg.toFloat()

                // If caffeine fully decays, reset initial values to stop further calculations in the loop
                if (_displayedCaffeineMg.floatValue == 0f) {
                    _initialCaffeineMg.floatValue = 0f
                }
                delay(1000) // Update every second
            }
        }
    }

    fun addCaffeine(amount: Int) {
        if (amount <= 0) return

        val currentTimeMillis = System.currentTimeMillis()
        val timeElapsedSinceLastIngestionMillis = (currentTimeMillis - _lastIngestionTimeMillis.longValue).toDouble()

        val remainingFromPrevious = if (_initialCaffeineMg.floatValue > 0f && _lastIngestionTimeMillis.longValue > 0L && timeElapsedSinceLastIngestionMillis > 0) {
            _initialCaffeineMg.floatValue * (0.5).pow(timeElapsedSinceLastIngestionMillis / CAFFEINE_HALF_LIFE_MILLIS_VM)
        } else {
            0.0
        }

        val newTotalInitialMg = (remainingFromPrevious + amount).toFloat()

        _initialCaffeineMg.floatValue = newTotalInitialMg
        _lastIngestionTimeMillis.longValue = currentTimeMillis
        _displayedCaffeineMg.floatValue = newTotalInitialMg // Update display immediately

        startOrRestartDecayCalculation()
    }

    // Call this when the ViewModel is about to be cleared
    override fun onCleared() {
        super.onCleared()
        decayCalculationJob?.cancel() // Cancel the coroutine when ViewModel is cleared
    }

    // --- Persistence methods (to be implemented) ---
    // fun loadCaffeineState() {
    //     viewModelScope.launch {
    //         // Load _initialCaffeineMg and _lastIngestionTimeMillis from DataStore/Room
    //         // Then call startContinuousDecayCalculation or ensure it's running
    //     }
    // }
    //
    // fun saveCaffeineState() {
    //     viewModelScope.launch {
    //         // Save _initialCaffeineMg and _lastIngestionTimeMillis to DataStore/Room
    //     }
    // }
}