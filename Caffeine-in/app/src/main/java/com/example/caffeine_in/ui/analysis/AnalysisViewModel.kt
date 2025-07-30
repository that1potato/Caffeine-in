package com.example.caffeine_in.ui.analysis

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.caffeine_in.data.CaffeineIntake
import com.example.caffeine_in.data.DataRepository
import com.example.caffeine_in.data.HistogramData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.pow

const val CAFFEINE_HALF_LIFE_HOURS_ANALYSIS = 5.0
const val CAFFEINE_HALF_LIFE_MILLIS_ANALYSIS = CAFFEINE_HALF_LIFE_HOURS_ANALYSIS * 60 * 60 * 1000

class AnalysisViewModel(application: Application) : AndroidViewModel(application) {

    private val dataRepository = DataRepository(application)

    // StateFlow for the Intake List (logged entries)
    private val _intakeList = MutableStateFlow<List<CaffeineIntake>>(emptyList())
    val intakeList: StateFlow<List<CaffeineIntake>> = _intakeList.asStateFlow()

    init {
        viewModelScope.launch {
            dataRepository.intakeListFlow.collect { intakes ->
                _intakeList.value = intakes
            }
        }
    }

    // get histogram data for the past 24hrs 2hr intervals
    fun getHistogramData(): List<HistogramData> {
        val currentTime = System.currentTimeMillis()
        val twoHoursInMillis = 2 * 60 * 60 * 1000L

        val histogramData = mutableListOf<HistogramData>()

        for (i in 0..11) {  // 12 intervals 24/2
            val intervalEnd = currentTime - (i * twoHoursInMillis)

            // caffeine level at the end of this interval
            val caffeineLevel = calculateCaffeineLevelAtTime(intervalEnd)

            // Format time label using proper calendar calculation
            val calendar = java.util.Calendar.getInstance()
            calendar.timeInMillis = intervalEnd
            val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
            val timeLabel = String.format(Locale.getDefault(), "%02d:00", hour)

            histogramData.add(HistogramData(timeLabel, caffeineLevel))
        }

        return histogramData.reversed() // Reverse to show oldest to newest
    }

    private fun calculateCaffeineLevelAtTime(targetTime: Long): Float {
        val intakes = _intakeList.value
        var totalCaffeine = 0f

        // Sum up all intakes that occurred before or at the target time
        for (intake in intakes) {
            if (intake.timestampMillis <= targetTime) {
                val timeElapsed = targetTime - intake.timestampMillis
                val decayedAmount = intake.amount * (0.5).pow(timeElapsed.toDouble() / CAFFEINE_HALF_LIFE_MILLIS_ANALYSIS)
                totalCaffeine += decayedAmount.toFloat()
            }
        }

        return if (totalCaffeine < 1) 0f else totalCaffeine
    }
}
