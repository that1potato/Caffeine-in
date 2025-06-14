package com.example.caffeine_in.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "caffeine_settings")

class DataRepository(private val context: Context) {

    private object PreferencesKeys {
        val INITIAL_CAFFEINE_MG = floatPreferencesKey("initial_caffeine_mg")
        val LAST_INGESTION_TIME_MILLIS = longPreferencesKey("last_ingestion_time_millis")
    }

    val caffeineStateFlow: Flow<Pair<Float, Long>> = context.dataStore.data
        .map { preferences ->
            val initialMg = preferences[PreferencesKeys.INITIAL_CAFFEINE_MG] ?: 0f
            val lastIngestion = preferences[PreferencesKeys.LAST_INGESTION_TIME_MILLIS] ?: 0L
            Pair(initialMg, lastIngestion)
        }

    suspend fun saveCaffeineState(initialMg: Float, lastIngestionTimeMillis: Long) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.INITIAL_CAFFEINE_MG] = initialMg
            settings[PreferencesKeys.LAST_INGESTION_TIME_MILLIS] = lastIngestionTimeMillis
        }
    }

    suspend fun clearCaffeineState() {
        context.dataStore.edit { settings ->
            settings.remove(PreferencesKeys.INITIAL_CAFFEINE_MG)
            settings.remove(PreferencesKeys.LAST_INGESTION_TIME_MILLIS)
        }
    }
}