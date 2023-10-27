package com.example.android.roomwordssample

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.android.roomwordssample.viewmodels.TaskViewModel
import com.example.android.roomwordssample.viewmodels.TaskViewModelFactory
import java.util.Locale

class DairyActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as PlannerApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dairy) // Assuming you have an XML layout file with a TextView with id 'diary'

        val dairyTextView: TextView = findViewById(R.id.diary)

        taskViewModel.allTasksForDairy.observe(this) { tasks ->
            if (tasks.isNotEmpty()) {
                val contentBuilder = StringBuilder()
                val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                tasks.forEach { task ->
                    // task.date is a String in YYYY-MM-DD format
                    val inputDate = inputDateFormat.parse(task.date)
                    val formattedDate = outputDateFormat.format(inputDate)
                    val taskDetail = "On $formattedDate, at ${task.time}, I ${task.name} for about ${task.duration} minutes."
                    contentBuilder.append(taskDetail).append("\n")
                }
                dairyTextView.text = contentBuilder.toString()
            } else {
                dairyTextView.text = "No tasks available."
            }
        }
    }
}
