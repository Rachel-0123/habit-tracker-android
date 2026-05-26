package com.example.habittracker.ui.theme.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habittracker.ui.theme.data.Habit
import java.time.DayOfWeek
import java.time.LocalDate


@Composable
fun CalendarScreen(viewModel: CalendarViewModel = viewModel()) {


    val daysInMonth = viewModel.getDaysInMonth()
    val today = LocalDate.now()
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, start = 16.dp, end = 16.dp)
    ) {

        Text(
            text = today.month.toString(),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(40.dp))


        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            //Days of the week display
            items(DayOfWeek.entries) { day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = day.name.take(3)
                    )
                }
            }

            //Fitting the days correctly according to the month
            items(viewModel.daysToAdd()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(" ")
                }
            }


            //Displaying dates of the month
            items(daysInMonth) { date ->

                val borderColor = if (date == today) Color.DarkGray else Color.LightGray
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    //Box for each day of the month
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(2.dp, color = borderColor, RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable(onClick = {
                                selectedDate.value = date
                            }),
                        contentAlignment = Alignment.TopCenter
                    ) {

                        Text("${date.dayOfMonth}")

                        //4 squares of colors for checked habits
                        Column()
                        {
                            Spacer(modifier = Modifier.height(32.dp))


                            val habitsCompleted = mutableStateListOf<Habit>()

                            for (habit in viewModel.habits) {
                                if (date.toString() in habit.completedDates) {
                                    habitsCompleted.add(habit)
                                }
                            }
                            Row {
                                for (habit in habitsCompleted.take(2)) {
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .size(12.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Color(habit.color))
                                    )
                                }
                            }
                            Row {
                                for (habit in habitsCompleted.drop(2).take(2)) {
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .size(12.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Color(habit.color))
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
        if (selectedDate.value != null) {

            Dialog(onDismissRequest = { selectedDate.value = null }) {


                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (habit in viewModel.habits) {
                            if (selectedDate.value.toString() in habit.completedDates)
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(habit.color))
                                )
                        }
                    }
                }

            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    CalendarScreen()
}


