package com.example.habittracker

import android.R.attr.checked
import android.R.attr.enabled
import android.R.attr.onClick
import androidx.compose.ui.unit.dp
import android.os.Bundle
import android.widget.CheckBox
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.habittracker.ui.theme.HabitTrackerTheme
import com.example.habittracker.ui.theme.MainActivityViewModel
import kotlin.getValue
import kotlin.properties.ReadOnlyProperty
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChecklistScreen()
                }
            }
        }
    }
}


@Composable
fun ChecklistScreen(viewModel: MainActivityViewModel = viewModel()) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "My Habit Tracker", style = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.DarkGray
            )
        )



        Text("Todays Date")


        val checked = viewModel.checked
        val progress = viewModel.progress
        val habits = viewModel.habits


        //  val listOfHabits = remember { mutableStateListOf<String>() }


        Text("$checked completed today")

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val habitTextStyle = TextStyle(
                fontWeight = FontWeight.Bold, fontSize = 16.sp
            )

            Column() {
                viewModel.habits.forEachIndexed { index, name ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = habits[index].isChecked,
                            onCheckedChange = { viewModel.toggleHabbit(index, it) }
                        )
                        Text(viewModel.habits[index].name)
                    }
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
            ) {

                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(progress)
                        .background(Color.Green, shape = RoundedCornerShape(12.dp))

                )

            }

        }

        val showDialog = remember { mutableStateOf(false) }

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
        val newHabitText = remember { mutableStateOf("") }
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
                        })
                })
        }

    }

}


@Preview(showBackground = true)
@Composable
fun ChecklistScreenPreview() {
    ChecklistScreen()
}