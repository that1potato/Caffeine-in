package com.example.caffeine_in.data

import kotlinx.serialization.Serializable

@Serializable
data class CaffeineSource(
    val name: String,
    val amount: Int,
)