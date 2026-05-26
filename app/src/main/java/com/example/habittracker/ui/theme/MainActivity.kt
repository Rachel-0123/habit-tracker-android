package com.example.habittracker.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.habittracker.ui.theme.calendar.CalendarScreen
import com.example.habittracker.ui.theme.dailyTracker.HomeScreen


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            HabitTrackerTheme {
                HabitTrackerApp()
            }
        }
    }
}
@PreviewScreenSizes
@Composable
fun HabitTrackerApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.DAILY_TRACKER) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = { Icon(it.icon, contentDescription = it.label)},
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }){
        when (currentDestination){
            AppDestinations.DAILY_TRACKER -> HomeScreen()
            AppDestinations.MONTHLY_TRACKER -> CalendarScreen()
        }
    }

}
enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    DAILY_TRACKER("Daily Tracker", Icons.Default.Home),
    MONTHLY_TRACKER("Monthly Tracker", Icons.Default.DateRange),
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
