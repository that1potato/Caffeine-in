package com.example.caffeine_in.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "caffeine_settings")

class DataRepository(private val context: Context) {

    private object PreferencesKeys {
        val INITIAL_CAFFEINE_MG = floatPreferencesKey("initial_caffeine_mg")
        val LAST_INGESTION_TIME_MILLIS = longPreferencesKey("last_ingestion_time_millis")
        val HISTORY_LIST = stringPreferencesKey("caffeine_history_list")
    }

    // caffeine decay calculation state
    val caffeineStateFlow: Flow<Pair<Float, Long>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val initialMg = preferences[PreferencesKeys.INITIAL_CAFFEINE_MG] ?: 0f
            val lastIngestion = preferences[PreferencesKeys.LAST_INGESTION_TIME_MILLIS] ?: 0L
            Pair(initialMg, lastIngestion)
        }

    // caffeine source list
    val historyListFlow: Flow<List<CaffeineSource>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val jsonString = preferences[PreferencesKeys.HISTORY_LIST] ?: "[]"
            Json.decodeFromString<List<CaffeineSource>>(jsonString)
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

    suspend fun addHistoryItem(source: CaffeineSource) {
        context.dataStore.edit { preferences ->
            val jsonString = preferences[PreferencesKeys.HISTORY_LIST] ?: "[]"
            val currentList = Json.decodeFromString<MutableList<CaffeineSource>>(jsonString)
            // Prevent duplicates based on name
            if (!currentList.any { it.name.equals(source.name, ignoreCase = true) }) {
                currentList.add(0, source) // Add new items to the top
                preferences[PreferencesKeys.HISTORY_LIST] = Json.encodeToString(currentList)
            }
        }
    }

    suspend fun removeHistoryItem(source: CaffeineSource) {
        context.dataStore.edit { preferences ->
            val jsonString = preferences[PreferencesKeys.HISTORY_LIST] ?: "[]"
            val currentList = Json.decodeFromString<MutableList<CaffeineSource>>(jsonString)
            currentList.removeAll { it.name.equals(source.name, ignoreCase = true) }
            preferences[PreferencesKeys.HISTORY_LIST] = Json.encodeToString(currentList)
        }
    }
}