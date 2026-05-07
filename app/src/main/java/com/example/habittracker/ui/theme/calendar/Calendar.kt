package com.example.habittracker.ui.theme.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.nio.file.WatchEvent
import java.time.DayOfWeek
import java.time.LocalDate


@Composable
fun CalendarScreen(viewModel: CalendarViewModel = viewModel()) {


    val daysInMonth = viewModel.getDaysInMonth()
    val today = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, start = 16.dp, end = 16.dp)
    ) {

        Text(text = today.month.toString(),
            style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(40.dp))


        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            items(DayOfWeek.entries) { day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = day.name.take(3)
                    )
                }
            }


            items(viewModel.daysToAdd()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(" ")
                }
            }


            items(daysInMonth) { date ->

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                        contentAlignment = Alignment.TopCenter){
                        Text("${date.dayOfMonth}")
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


