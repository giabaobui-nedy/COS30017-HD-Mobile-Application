package com.example.android.roomwordssample.repository

import androidx.annotation.WorkerThread
import com.example.android.roomwordssample.database.dao.TaskDAO
import com.example.android.roomwordssample.database.entity.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository (private val taskDao: TaskDAO) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    val allTasksForToday: Flow<List<Task>> = taskDao.getTasksOfToday()
    val allTasksForDairy: Flow<List<Task>> = taskDao.getTasksForDairy()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun setTaskStatus(taskId: Int, isDone: Boolean) {
        taskDao.setTaskStatus(taskId, isDone)
    }

}