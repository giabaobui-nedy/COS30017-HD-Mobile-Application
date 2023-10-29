package com.example.android.roomwordssample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.example.android.roomwordssample.database.entity.Task
import com.example.android.roomwordssample.viewmodels.TaskViewModel
import com.example.android.roomwordssample.viewmodels.TaskViewModelFactory
import kotlin.Exception

class TaskDetailActivity : AppCompatActivity(), ConfirmDeleteDialog.NoticeDialogListener {

    private val confirmed: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as PlannerApplication).repository)
    }
    private fun confirmDeleteDialog() {
        // Create an instance of the dialog fragment and show it.
        val dialog = ConfirmDeleteDialog()
        dialog.show(supportFragmentManager, "ConfirmDeleteDialog")
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following
    // methods defined by the NoticeDialogFragment.NoticeDialogListener
    // interface.
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        confirmed.value = true
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val task:Task? = intent.extras?.getParcelable("Task")

        /* Connect variables to UI elements. */
        val taskName: EditText = findViewById(R.id.task_detail_name)
        val taskDetail: EditText = findViewById(R.id.task_detail_description)
        val taskDate: EditText = findViewById(R.id.task_detail_date)
        val taskStartTime: EditText = findViewById(R.id.task_detail_time)
        val taskDuration: EditText = findViewById(R.id.task_detail_duration)
        val taskFrequency: EditText = findViewById(R.id.task_detail_frequency)

        val updateButton: Button = findViewById(R.id.update_task)
        val deleteButton: Button = findViewById(R.id.delete_task)
        val redoButton: Button = findViewById(R.id.redo_task)


        task?.let {
            taskName.setText(task.name)
            taskDetail.setText(task.detail)
            taskDate.setText(task.date)
            taskStartTime.setText(task.time)
            taskDuration.setText(task.duration.toString())
            taskFrequency.setText(task.frequency)
            redoButton.isEnabled = task.isDone
        }

        redoButton.setOnClickListener {
            task?.let {
                if (task.isDone) {
                    try {
                        taskViewModel.setTaskStatus(task.id, false)
                        Toast.makeText(this, "Task is redo!", Toast.LENGTH_SHORT).show()
                        val replyIntent = Intent()
                        setResult(Activity.RESULT_OK, replyIntent)
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Problem with the database", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        updateButton.setOnClickListener {
            if (task != null) {

                var newName = task.name
                var newDetail = task.detail
                var newDate = task.date
                var newTime = task.time
                var newDuration = task.duration
                var newFrequency = task.frequency

                var valid = true

                if (taskName.text.toString() != task.name) {
                    if (taskName.text.toString() == "") {
                        taskName.error = "Please not leave the name field empty"
                        valid = false
                    } else {
                        newName = taskName.text.toString()
                    }
                }

                if (taskDetail.text.toString() != task.detail) {
                    newDetail = taskDetail.text.toString()
                }

                if (taskDate.text.toString() != task.date) {
                    if (taskDate.text.toString() != "") {
                        val regexPattern = Regex("""^\d{4}-\d{2}-\d{2}$""")
                        if (!regexPattern.matches(taskDate.text.toString())) {
                            taskDate.error = "Please enter the date in this format: 'YYYY-MM-DD'"
                            valid = false
                        } else {
                            newDate = taskDate.text.toString()
                        }
                    } else {
                        taskDate.error = "Please not leave the starting date field empty"
                        valid = false
                    }
                }

                if (taskStartTime.text.toString() != task.time) {
                    if (taskStartTime.text.toString() != "") {
                        // Define a regular expression pattern for time in the HH:MM format (24-hour clock).
                        val timeRegexPattern = Regex("""^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$""")
                        if (!timeRegexPattern.matches(taskStartTime.text.toString())) {
                            taskStartTime.error = "Please enter the time in this format: 'HH:MM' (24-hour clock)"
                            valid = false
                        } else {
                            newTime = taskStartTime.text.toString()
                        }
                    } else {
                        taskStartTime.error = "Please do not leave the starting time field empty"
                        valid = false
                    }
                }

                if (taskDuration.text.toString() != task.duration.toString()) {
                    try {
                        if (taskDuration.text.toString().toInt() <= 0) {
                            taskDuration.error = "Please, You must do something more than one minute"
                            valid = false
                        } else {
                            newDuration = taskDuration.text.toString().toInt()
                        }
                    } catch (e:Exception) {
                        taskDuration.error = "Seems like it is not in a valid number format"
                        valid = false
                    }
                }

                if (taskFrequency.text.toString().lowercase() == "once") {
                    newFrequency = "Once"
                } else if (taskFrequency.text.toString().lowercase() == "daily") {
                    newFrequency = "Daily"
                } else {
                    taskFrequency.error = "Currently, the app only supports task occurring once or daily"
                    valid = false
                }

                if (valid) {
                    val updatedTask:Task = Task(task.id, newName, newDetail, newDate, newTime, newDuration, newFrequency, task.isDone)

                    try {
                        taskViewModel.updateTask(updatedTask)
                        Toast.makeText(this, "Update successfully!", Toast.LENGTH_SHORT).show()
                    } catch (e:Exception) {
                        Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Update failed due to invalid inputs!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        deleteButton.setOnClickListener {
            confirmDeleteDialog()
        }

        fun deleteTask() {
            task?.let {
                try {
                    taskViewModel.deleteTask(task.id)
                    Toast.makeText(this, "Delete successfully!", Toast.LENGTH_SHORT).show()
                    val replyIntent = Intent()
                    setResult(Activity.RESULT_OK, replyIntent)
                    finish()
                } catch (e:Exception) {
                    Toast.makeText(this, "Delete failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        confirmed.observe(this) { confirmed ->
            if (confirmed) {
                deleteTask()
            }
        }
    }
}
