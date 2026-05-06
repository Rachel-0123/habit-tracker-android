package com.example.habittracker.ui.theme

import android.app.Application
import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataStoreManager(private val context: Context) {

    val Context.dataStore by preferencesDataStore("habit_prefs")
    private val HABITS_KEY = stringPreferencesKey("habits_key")

    suspend fun saveHabits(habits: List<Habit>) {
        val json = Json.encodeToString(habits)

        context.dataStore.edit { prefs ->
            prefs[HABITS_KEY] = json
        }
    }

    fun loadHabits(): Flow<List<Habit>> {
        return context.dataStore.data.map { prefs ->
            val json = prefs[HABITS_KEY] ?: return@map emptyList()
            Json.decodeFromString(json)

        }
    }


}