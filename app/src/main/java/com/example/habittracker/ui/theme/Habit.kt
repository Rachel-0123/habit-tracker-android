package com.example.habittracker.ui.theme

import kotlinx.serialization.Serializable


@Serializable
data class Habit(
    val name: String,
    val completedDates: List<String> = emptyList()
)