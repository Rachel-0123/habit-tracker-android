package com.example.habittracker.ui.theme

import android.R.attr.checked
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateListOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import com.example.habittracker.R

class MainActivityViewModel : ViewModel() {

    var habits = mutableStateListOf<Habit>()
        private set

    val checked: Int
        get() = habits.count { it.isChecked }

    val progress
        get() = if (habits.isEmpty()) 0f else checked / habits.size.toFloat()

    fun addHabit(name: String) {
        habits.add(Habit(name))
    }

    fun toggleHabbit(index: Int, value: Boolean) {
        habits[index] = habits[index].copy(isChecked = value)
    }
}