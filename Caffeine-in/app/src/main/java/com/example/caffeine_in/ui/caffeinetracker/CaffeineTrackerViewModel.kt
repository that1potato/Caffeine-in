package com.example.caffeine_in.ui.caffeinetracker

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        if (_initialCaffeineMg.floatValue == 0f && _displayedCaffeineMg.floatValue == 0f) {
            val simulatedInitialAmount = 250
            if (simulatedInitialAmount > 0) {
                _initialCaffeineMg.floatValue = simulatedInitialAmount.toFloat()
                _lastIngestionTimeMillis.longValue = System.currentTimeMillis()
                _displayedCaffeineMg.floatValue = simulatedInitialAmount.toFloat()
            }
        }
        startContinuousDecayCalculation()
    }

    private fun startContinuousDecayCalculation() {
        viewModelScope.launch {
            if (_initialCaffeineMg.floatValue == 0f) { // no looping if caffeine is 0
                _displayedCaffeineMg.floatValue = 0f
                return@launch
            }

            while (true) {
                val currentTimeMillis = System.currentTimeMillis()
                val timeElapsedMillis = (currentTimeMillis - _lastIngestionTimeMillis.longValue).toDouble()

                if (timeElapsedMillis < 0) {
                    _displayedCaffeineMg.floatValue = _initialCaffeineMg.floatValue
                    delay(1000)
                    continue
                }

                val currentCalculatedMg = _initialCaffeineMg.floatValue * (0.5).pow(timeElapsedMillis / CAFFEINE_HALF_LIFE_MILLIS_VM)
                _displayedCaffeineMg.floatValue = if (currentCalculatedMg < 1) 0f else currentCalculatedMg.toFloat()

                // reset _initialCaffeineMg if fully decayed
                if (_displayedCaffeineMg.floatValue == 0f && _initialCaffeineMg.floatValue > 0f) {
                    _initialCaffeineMg.floatValue = 0f
                }
                delay(1000) // Update every second
            }
        }
    }

    fun addCaffeine(amount: Int) {
        val currentTimeMillis = System.currentTimeMillis()
        val timeElapsedSinceLastIngestionMillis = (currentTimeMillis - _lastIngestionTimeMillis.longValue).toDouble()

        val remainingFromPrevious = if (_initialCaffeineMg.floatValue > 0f && _lastIngestionTimeMillis.longValue > 0L) {
            _initialCaffeineMg.floatValue * (0.5).pow(timeElapsedSinceLastIngestionMillis / CAFFEINE_HALF_LIFE_MILLIS_VM)
        } else {
            0.0
        }

        val newTotal = (remainingFromPrevious + amount).toFloat()
        _initialCaffeineMg.floatValue = if (newTotal < 1f && amount > 0) amount.toFloat() else newTotal
        _lastIngestionTimeMillis.longValue = currentTimeMillis
        _displayedCaffeineMg.floatValue = _initialCaffeineMg.floatValue // Update display immediately

        // Restart the decay calculation if it wasn't running or if parameters changed significantly
        // The LaunchedEffect in Composable was keyed, here we manage the coroutine.
        // A simple way is to rely on the fact that if _initialCaffeineMg was 0,
        // a new call to startContinuousDecayCalculation might be needed,
        // or ensure the existing loop picks up the new values.
        // For this structure, if the while loop is already running and initialCaffeineMg was >0,
        // it will naturally use the new _initialCaffeineMg and _lastIngestionTimeMillis.
        // If _initialCaffeineMg became >0 from 0, we might need to kickstart.
        if (remainingFromPrevious <= 0.0 && amount > 0 && viewModelScope.coroutineContext[kotlinx.coroutines.Job]?.isActive != true) {
            // This condition might need refinement depending on how you stop the loop.
            // Or simply let the existing loop (if designed robustly) pick up changes.
            // The key in LaunchedEffect handles this more declaratively.
            // Here, a single, long-running coroutine is often preferred.
            // The current `startContinuousDecayCalculation` will just launch another if called again,
            // which is not ideal. Better to manage a single Job.

            // A more robust approach for restarting or ensuring one instance:
            // job?.cancel()
            // job = viewModelScope.launch { ... }
            // For simplicity here, we assume the existing loop will adapt or that
            // initial state is properly set up.
        }
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