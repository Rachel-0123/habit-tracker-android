package com.example.habittracker.ui.theme.dailyTracker

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habittracker.ui.theme.Background
import com.example.habittracker.ui.theme.PrimaryGreen
import com.example.habittracker.ui.theme.TextSecondary
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: DailyTrackerViewModel = viewModel()) {



    val todaysChecked = viewModel.todaysCheckedHabits
    val progress = viewModel.progress
    val habits = viewModel.habits
    val newHabitText = remember { mutableStateOf("") }
    val showEditSheet = remember { mutableStateOf(false) }
    val selectedHabitIndex = remember { mutableStateOf(-1) }
    val editHabitText = remember { mutableStateOf("") }
    val showAddSheet = remember { mutableStateOf(false) }
    val selectedColor = remember { mutableStateOf(Color(0xFF4CAF50)) }
    val habitColors = listOf(

        Color(0xFFF44336), // Red
        Color(0xFFDA8302), // Orange
        Color(0xFFFFC107), // Amber
        Color(0xFFCDDC39), // Lime
        Color(0xFF4CAF50), // Green
        Color(0xFF00BCD4), // Cyan
        Color(0xFF2196F3), // Blue
        Color(0xFF3F51B5), // Indigo
        Color(0xFF9C27B0), // Purple
        Color(0xFFD72D64)  // Pink
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(top = 40.dp)
            .padding(horizontal = 20.dp),
    ) {



        //Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "My Habits",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.SansSerif
            )
            IconButton(
                onClick = { showAddSheet.value = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Habit"
                )
            }
        } //Header

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
        Spacer(modifier = Modifier.height(16.dp))

        //Habit list
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
        ) {
            itemsIndexed(viewModel.habits) { index, habit ->
                val isChecked = viewModel.isHabitChecked(index)
                val borderColor = if (isChecked) Color(habit.color) else Background
                val scale by animateFloatAsState(
                    if (isChecked) 1.05f else 1f,
                    label = "scale"
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { viewModel.toggleHabit(index) },
                            onLongClick = {
                                selectedHabitIndex.value = index
                                editHabitText.value = habit.name
                                showEditSheet.value = true
                            }
                        )
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(width = 3.dp, color = borderColor)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        val animatedColor by animateColorAsState(
                            (if (isChecked) Color(habit.color) else Color.LightGray),
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
                                        .clickable { viewModel.toggleHabit(index) }
                                )
                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = habit.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(habit.color)
                                )
                            }
                            Text(
                                text = "🔥 ${viewModel.streakCalculator(habit)}",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))


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
                                                Color(habit.color)
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
        if (showAddSheet.value) {
            ModalBottomSheet(onDismissRequest = { showAddSheet.value = false }) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp), Arrangement.spacedBy(20.dp)
                ) {
                    Text("New Habit")
                    TextField(
                        value = newHabitText.value,
                        onValueChange = { newText: String ->
                            newHabitText.value = newText
                        },
                        placeholder = { Text("Habit Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 24.dp),
                    text = "Choose Color",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .horizontalScroll(rememberScrollState()),

                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    habitColors.forEach { color ->

                        val isSelected =
                            selectedColor.value == color

                        Box(
                            modifier = Modifier
                                .size(
                                    if (isSelected) 42.dp else 34.dp
                                )
                                .clip(CircleShape)
                                .background(color)
                                .clickable {
                                    selectedColor.value = color
                                }
                        )
                    }
                }

                Button(
                    onClick = {

                        viewModel.addHabit(
                            name = newHabitText.value,
                            color = selectedColor.value
                        )

                        newHabitText.value = ""

                        showAddSheet.value = false
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(24.dp),
                    enabled = newHabitText.value.isNotBlank()
                ) {
                    Text("Create Habit")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showEditSheet.value && selectedHabitIndex.value != -1) {
        ModalBottomSheet(
            onDismissRequest = { showEditSheet.value = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Edit Habit",
                    style = MaterialTheme.typography.headlineSmall
                )
                TextField(
                    value = editHabitText.value,
                    onValueChange = { editHabitText.value = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Color",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    habitColors.forEach { color ->

                        val isSelected =
                            selectedColor.value == color

                        Box(
                            modifier = Modifier
                                .size(
                                    if (isSelected) 42.dp else 34.dp
                                )
                                .clip(CircleShape)
                                .background(color)
                                .clickable {
                                    selectedColor.value = color
                                }
                        )
                    }
                }


                Button(
                    onClick = {
                        viewModel.updateHabit(
                            selectedHabitIndex.value,
                            editHabitText.value,
                            selectedColor.value
                        )
                        showEditSheet.value = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }

                OutlinedButton(
                    onClick = {
                        viewModel.deleteHabit(selectedHabitIndex.value)
                        showEditSheet.value = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Habit")
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
