package com.example.caffeine_in.data

import kotlinx.serialization.Serializable

@Serializable
data class CaffeineSource(
    // ensured items in the list will have non-duplicating names
    val name: String,
    val amount: Int,
)