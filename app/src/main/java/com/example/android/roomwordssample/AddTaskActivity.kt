package com.example.android.roomwordssample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.android.roomwordssample.database.entity.Task
import com.example.android.roomwordssample.viewmodels.TaskViewModel
import com.example.android.roomwordssample.viewmodels.TaskViewModelFactory
import com.google.android.material.snackbar.Snackbar


class AddTaskActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as PlannerApplication).repository)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val taskNameEditText = findViewById<EditText>(R.id.task_name)
        val taskDateEditText = findViewById<EditText>(R.id.task_date)
        val taskTimeEditText = findViewById<EditText>(R.id.task_time)
        val taskDurationSeekBar = findViewById<SeekBar>(R.id.task_duration)
        val taskFrequencyRadioGroup = findViewById<RadioGroup>(R.id.task_frequency)
        val taskDetailEditText = findViewById<EditText>(R.id.task_detail)

        //add button
        val addButton = findViewById<Button>(R.id.add_task)
        val addRandomTaskButton = findViewById<Button>(R.id.add_random_task)

        addRandomTaskButton.setOnClickListener {
            val task = Task (
                    id = 0, // If you're auto-generating the ID in the database, you can set it to 0
                    name = "a sample task",
                    detail = "a sample detail",
                    date = "2023-11-07",
                    time = "12:00", // Set the time as needed
                    duration = 45,
                    frequency = "Once",
                    isDone = false
            )
            for (i in 0..9) {
                taskViewModel.addTask(task)
            }
            Toast.makeText(this, "Successfully added 10 tasks", Toast.LENGTH_LONG).show()
            val replyIntent = Intent()
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }

        addButton.setOnClickListener {

            val taskName = taskNameEditText.text.toString()
            val taskDateText = taskDateEditText.text.toString()
            val taskTimeText = taskTimeEditText.text.toString()
            val taskDuration = taskDurationSeekBar.progress
            val selectedFrequencyRadioButtonId = taskFrequencyRadioGroup.checkedRadioButtonId
            val selectedFrequencyRadioButton = findViewById<RadioButton>(selectedFrequencyRadioButtonId)
            var taskFrequency = ""
            selectedFrequencyRadioButton?.let {
                taskFrequency = selectedFrequencyRadioButton.text.toString()
            }
            val taskDetail = taskDetailEditText.text.toString()

            var valid = true

            if (taskName == "") {
                taskNameEditText.error = "Please not leave the name field empty"
                valid = false
            }

            if (taskDateText != "") {
                val regexPattern = Regex("""^\d{4}-\d{2}-\d{2}$""")
                if (!regexPattern.matches(taskDateText)) {
                    taskDateEditText.error = "Please enter the date in this format: 'YYYY-MM-DD'"
                    valid = false
                }
            } else {
                taskDateEditText.error = "Please not leave the starting date field empty"
                valid = false
            }

            if (taskTimeText != "") {
                // Define a regular expression pattern for time in the HH:MM format (24-hour clock).
                val timeRegexPattern = Regex("""^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$""")
                if (!timeRegexPattern.matches(taskTimeText)) {
                    taskTimeEditText.error = "Please enter the time in this format: 'HH:MM' (24-hour clock)"
                    valid = false
                }
            } else {
                taskTimeEditText.error = "Please do not leave the starting time field empty"
                valid = false
            }

            if (taskDuration == 0) {
                val snackBar = Snackbar.make(findViewById(R.id.add_task_layout), "Duration must be longer than 0", Snackbar.LENGTH_LONG).show()
                valid = false
            }

            if (taskFrequency == "") {
                val snackBar2 = Snackbar.make(findViewById(R.id.add_task_layout), "You must select between two options", Snackbar.LENGTH_LONG).show()
                valid = false
            }

            if (valid) {
                val task = Task (
                    id = 0, // If you're auto-generating the ID in the database, you can set it to 0
                    name = taskName,
                    detail = taskDetail,
                    date = taskDateText,
                    time = taskTimeText, // Set the time as needed
                    duration = taskDuration,
                    frequency = taskFrequency,
                    isDone = false
                )
                taskViewModel.addTask(task)
                Toast.makeText(this, "Successfully added", Toast.LENGTH_LONG).show()
                val replyIntent = Intent()
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }
}