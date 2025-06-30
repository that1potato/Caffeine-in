package com.example.caffeine_in.data

// data class for a single setting item
data class SettingItem(
    val title: String,
    val subtitle: String? = null,
    val hasSwitch: Boolean = false,
    val isSwitchEnabled: Boolean = false,
    val onSwitchChange: ((Boolean) -> Unit)? = null,
    val onClick: () -> Unit = {}
)