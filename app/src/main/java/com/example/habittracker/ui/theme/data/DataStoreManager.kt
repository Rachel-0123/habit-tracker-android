package com.example.habittracker.ui.theme.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore("habit_prefs")

class DataStoreManager(private val context: Context) {


    private val HABITS_KEY = stringPreferencesKey("habits_key")



    suspend fun saveHabits(habits: List<Habit>) {
        val json = Json.Default.encodeToString(habits)

        context.dataStore.edit { prefs ->
            prefs[HABITS_KEY] = json
        }
    }

    fun loadHabits(): Flow<List<Habit>> {
        return context.dataStore.data.map { prefs ->
            val json = prefs[HABITS_KEY] ?: return@map emptyList()
            Json.Default.decodeFromString(json)

        }
    }


}