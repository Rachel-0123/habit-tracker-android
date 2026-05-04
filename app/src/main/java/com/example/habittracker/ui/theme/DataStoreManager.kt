package com.example.habittracker.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.serialization.Serializable

class DataStoreManager {

    val Context.dataStore by preferencesDataStore("habit_prefs")
    val HABITS_KEY = stringPreferencesKey("habits")

    @Serializable
    data class Habit(
        val name: String,
        val isChecked: Boolean
    )

}