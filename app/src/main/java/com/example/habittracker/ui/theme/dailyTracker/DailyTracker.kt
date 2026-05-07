package com.example.habittracker.ui.theme.dailyTracker

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habittracker.ui.theme.Background
import com.example.habittracker.ui.theme.PrimaryGreen
import com.example.habittracker.ui.theme.TextPrimary
import com.example.habittracker.ui.theme.TextSecondary
import java.time.LocalDate

@Composable
fun HomeScreen(viewModel: DailyTrackerViewModel = viewModel()) {



    val todaysChecked = viewModel.todaysCheckedHabits
    val progress = viewModel.progress
    val habits = viewModel.habits
    val showDialog = remember { mutableStateOf(false) }
    val newHabitText = remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {

        //Header
        Text(
            "My Habits",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 24.sp,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = viewModel.formatDate(LocalDate.now()),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Progress section
        Text(
            text = "$todaysChecked of ${habits.size} completed",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))


        val animatedProgress by animateFloatAsState(progress, label = "progress")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFE5E5EA))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .background(PrimaryGreen)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        //Habit list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            itemsIndexed(viewModel.habits) { index, habit ->
                val isChecked = viewModel.isHabitChecked(index)
                val scale by animateFloatAsState(
                    if (isChecked) 1.05f else 1f,
                    label = "scale"
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleHabit(index, !isChecked) }
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {


                        val animatedColor by animateColorAsState(
                            if (isChecked) PrimaryGreen else Color.LightGray,
                            label = "color"
                        )
                        val animatedScale by animateFloatAsState(
                            if (isChecked) 1.2f else 1f,
                            label = "scale"
                        )


                        //Top row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .graphicsLayer {
                                            scaleX = animatedScale
                                            scaleY = animatedScale
                                        }
                                        .clip(CircleShape)
                                        .background(animatedColor)
                                        .clickable { viewModel.toggleHabit(index, !isChecked) }
                                )
                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = habit.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isChecked) PrimaryGreen else TextPrimary
                                )
                            }
                            Text(
                                text = "🔥 ${viewModel.streakCalculator(habit)}",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))


                        //Calendar row
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {


                            for (date in viewModel.getLast7days()) {
                                val isCompleted = date.toString() in habit.completedDates
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .padding(2.dp)
                                        .background(
                                            if (isCompleted) {
                                                PrimaryGreen
                                            } else {
                                                Color(0xFFCECECE)
                                            },
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    showDialog.value = true
                }, modifier = Modifier.weight(1f)
            ) {
                Text("Add Habit")
            }
            OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) {
                Text("Edit Habit")
            }
        }

        //Dialog
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addHabit(newHabitText.value)
                            newHabitText.value = ""
                            showDialog.value = false
                        },
                        enabled = newHabitText.value.isNotBlank(),
                    ) { Text("Save") }
                },
                dismissButton = {
                    Button(onClick = {
                        newHabitText.value = ""
                        showDialog.value = false
                    }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Add habit") },
                text = {
                    TextField(
                        value = newHabitText.value,
                        onValueChange = { newText ->
                            newHabitText.value = newText
                        },
                        placeholder = { Text("Enter habit name") })
                })
        }

    }
}
