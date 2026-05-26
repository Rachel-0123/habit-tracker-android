package com.example.habittracker.ui.theme.dailyTracker

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.ui.theme.data.DataStoreManager
import com.example.habittracker.ui.theme.data.Habit
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

var colorIndex = 0

class DailyTrackerViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application)

    var habits = mutableStateListOf<Habit>()
        private set

    fun today() = LocalDate.now().toString()
    //val today = LocalDate.now().toString()

    val todaysCheckedHabits: Int
        get() = habits.count { today() in it.completedDates }

    val progress
        get() = if (habits.isEmpty()) 0f else todaysCheckedHabits / habits.size.toFloat()

    fun addHabit(name: String, color: Color) {
        habits.add(Habit(name = name, color = color.toArgb()))
        viewModelScope.launch { dataStoreManager.saveHabits(habits) }
    }

    fun isHabitChecked(index: Int): Boolean {
        return today() in habits[index].completedDates
    }

    fun toggleHabit(index: Int) {
        if (isHabitChecked(index)) {
            habits[index] = habits[index].copy(
                name = habits[index].name,
                completedDates = habits[index].completedDates - today()
            )
        } else {
            habits[index] = habits[index].copy(
                name = habits[index].name,
                completedDates = habits[index].completedDates + today()
            )
        }
        viewModelScope.launch { dataStoreManager.saveHabits(habits) }
    }

    fun streakCalculator(habit: Habit): Int {

        var streak = 0
        var date = LocalDate.now()

        while (date.toString() in habit.completedDates) {
            streak++
            date = date.minusDays(1)
        }

        return streak

    }

    fun getLast7days(): List<LocalDate> {
        val today = LocalDate.now()
        var date = LocalDate.now().minusDays(6)
        val last7days = mutableListOf<LocalDate>()
        while (date <= today) {
            last7days.add(date)
            date = date.plusDays(1)
        }
        return last7days
    }

    fun formatDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
        return date.format(formatter)
    }
    fun updateHabit(index: Int, newText: String, color: Color){
        habits[index] = habits[index].copy(
            name = newText,
            color = color.toArgb()
        )
        viewModelScope.launch { dataStoreManager.saveHabits(habits) }
    }

    fun deleteHabit(index: Int){
        habits.removeAt(index)
        viewModelScope.launch { dataStoreManager.saveHabits(habits) }
    }


    init {
        viewModelScope.launch {
            dataStoreManager.loadHabits().collect {
                habits.clear()
                habits.addAll(it)
            }
            dataStoreManager.loadColorIndex().collect {
                colorIndex = it
            }
        }
    }
}