package com.example.android.roomwordssample.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.android.roomwordssample.database.entity.Task
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDAO {
    // get all tasks of today
    @Query("SELECT * FROM task_table WHERE date = date()")
    fun getTasksOfToday(): Flow<List<Task>>

    @Query("SELECT * FROM task_table")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE done = 1 ORDER BY date")
    fun getTasksForDairy(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("DELETE FROM task_table WHERE id = :taskId")
    fun deleteTask(taskId: Int)

    @Query("UPDATE task_table SET done = :isDone WHERE id = :taskId")
    fun setTaskStatus(taskId: Int, isDone: Boolean)

    @Query("DELETE FROM task_table")
    fun deleteAllTasks()
}