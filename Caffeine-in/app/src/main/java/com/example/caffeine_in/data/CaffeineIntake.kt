package com.example.caffeine_in.data

import kotlinx.serialization.Serializable

@Serializable
data class CaffeineIntake(
    val sourceName: String,
    val amount: Int,
    val timestampMillis: Long
)
