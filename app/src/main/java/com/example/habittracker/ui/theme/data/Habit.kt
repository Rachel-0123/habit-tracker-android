package com.example.habittracker.ui.theme.data

import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val name: String,
    val color: Int,
    val completedDates: List<String> = emptyList()
)