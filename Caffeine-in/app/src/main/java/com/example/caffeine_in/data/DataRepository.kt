package com.example.caffeine_in.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
        val APP_HAS_BEEN_LAUNCHED_BEFORE = booleanPreferencesKey("app_has_been_launched_before")
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
    
    // if app has been launched before
    val hasBeenLaunchedBeforeFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // It will default to 'false' if it has never been set.
            preferences[PreferencesKeys.APP_HAS_BEEN_LAUNCHED_BEFORE] ?: false
        }

    // caffeine source list
    val historyListFlow: Flow<List<CaffeineSource>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val jsonString = preferences[PreferencesKeys.HISTORY_LIST] ?: "[]"
            Json.decodeFromString<List<CaffeineSource>>(jsonString)
        }
    
    suspend fun setHasBeenLaunchedBefore() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_HAS_BEEN_LAUNCHED_BEFORE] = true
        }
    }

    // called on the first time the app launches
    suspend fun setHistoryList(historyList: List<CaffeineSource>) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HISTORY_LIST] = Json.encodeToString(historyList)
        }
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
    
    suspend fun insertHistoryItem(index: Int, source: CaffeineSource) {
        context.dataStore.edit { preferences ->
            val jsonString = preferences[PreferencesKeys.HISTORY_LIST] ?: "[]"
            val currentList = Json.decodeFromString<MutableList<CaffeineSource>>(jsonString)
            // prevent duplicates
            if (!currentList.any { it.name.equals(source.name, ignoreCase = true) }) {
                // ensure valid index
                if (index >= 0 && index <= currentList.size) {
                    currentList.add(index, source)
                    preferences[PreferencesKeys.HISTORY_LIST] = Json.encodeToString(currentList)
                }
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
    
    suspend fun updateHistoryItem(oldSource: CaffeineSource, newName: String, newAmount: Int): Boolean {
        var updated = false
        context.dataStore.edit { preferences ->
            val jsonString = preferences[PreferencesKeys.HISTORY_LIST] ?: "[]"
            val currentList = Json.decodeFromString<MutableList<CaffeineSource>>(jsonString)
            
            // if new name already exists
            val nameExists = currentList.any {
                it.name.equals(newName, ignoreCase = true) && it.name != oldSource.name
            }
            
            if (nameExists) {
                updated = false // Name already exists, do not proceed.
            } else {
                // update
                val itemIndex = currentList.indexOfFirst { it.name == oldSource.name }
                if (itemIndex != -1) {
                    currentList[itemIndex] = oldSource.copy(name = newName, amount = newAmount)
                    preferences[PreferencesKeys.HISTORY_LIST] = Json.encodeToString(currentList)
                    updated = true
                } else {
                    updated = false // not found, should not happen
                }
            }
        }
        return updated
    }
}