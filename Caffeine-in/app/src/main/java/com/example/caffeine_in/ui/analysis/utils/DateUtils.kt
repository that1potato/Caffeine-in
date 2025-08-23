package com.example.caffeine_in.ui.analysis.utils

import com.example.caffeine_in.data.CaffeineIntake
import java.text.SimpleDateFormat
import java.util.*

data class GroupedIntake(
    val dateLabel: String,
    val intakes: List<CaffeineIntake>
)

fun List<CaffeineIntake>.groupByDay(): List<GroupedIntake> {
    if (isEmpty()) return emptyList()
    
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
    
    // Group by day
    val grouped = groupBy { intake ->
        calendar.timeInMillis = intake.timestampMillis
        "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.DAY_OF_YEAR)}"
    }
    
    // Convert to list with proper labels
    return grouped.map { (_, intakes) ->
        val firstIntakeTime = intakes.first().timestampMillis
        val dateLabel = getDateLabel(firstIntakeTime, dateFormatter)
        GroupedIntake(
            dateLabel = dateLabel,
            intakes = intakes.sortedByDescending { it.timestampMillis }
        )
    }.sortedByDescending { it.intakes.first().timestampMillis }
}

private fun getDateLabel(timeMillis: Long, formatter: SimpleDateFormat): String {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    
    val yesterday = today - 24 * 60 * 60 * 1000L
    
    return when {
        timeMillis >= today -> "Today"
        timeMillis >= yesterday -> "Yesterday"
        else -> formatter.format(Date(timeMillis))
    }
}