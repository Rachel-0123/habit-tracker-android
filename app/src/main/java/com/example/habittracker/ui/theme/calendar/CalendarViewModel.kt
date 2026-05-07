package com.example.habittracker.ui.theme.calendar

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.ui.theme.data.DataStoreManager
import com.example.habittracker.ui.theme.data.Habit
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class CalendarViewModel(application: Application): AndroidViewModel(application){

    private val dataStoreManager = DataStoreManager(application)

    var habits = mutableStateListOf<Habit>()
        private set

    fun getDaysInMonth():List<LocalDate>{
        val today = LocalDate.now()
        val firstDay = today.withDayOfMonth(1)
        val lastDay = today.withDayOfMonth(today.lengthOfMonth())

        val days = mutableListOf<LocalDate>()
        var current = firstDay

        while (current <= lastDay){
            days.add(current)
            current = current.plusDays(1)
        }

        return days
    }

    fun daysToAdd():Int{
        val today = LocalDate.now()
        val firstDay = today.withDayOfMonth(1)
        return firstDay.dayOfWeek.value % 7
    }

    init {
        viewModelScope.launch {
            dataStoreManager.loadHabits().collect {
                habits.clear()




                habits.addAll(it)
            }
        }
    }



}
