package com.example.android.roomwordssample.viewmodels

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.roomwordssample.database.entity.Task
import com.example.android.roomwordssample.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    // val allTasks: LiveData<List<Task>> = repository.allTasks.asLiveData()
    private val allTasksForToday: LiveData<List<Task>> = repository.allTasksForToday.asLiveData()
    // LiveData for sorted tasks
    val sortedTasks: LiveData<List<Task>> = Transformations.map(allTasksForToday) { tasks ->
        // Use the helper function to sort tasks by time
        tasks.sortedBy { parseTimeToComparableFormat(it.time) }
    }

    // All tasks for Dairy Activity
    val allTasksForDiary: LiveData<List<Task>> = repository.allTasksForDiary.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun addTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.addTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTask(task)
    }

    fun deleteTask(taskId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTask(taskId)
    }

    fun setTaskStatus(taskId: Int, isDone:Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.setTaskStatus(taskId, isDone)
    }

    // Helper function to convert time strings to comparable format (e.g., Date)
    private fun parseTimeToComparableFormat(time: String): Date {
        // Implement your logic to parse the time string into a Date object or timestamp here.
        // You can use SimpleDateFormat or other date-time parsing methods.
        // For this example, let's assume you're using SimpleDateFormat.
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.parse(time) ?: Date()
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
