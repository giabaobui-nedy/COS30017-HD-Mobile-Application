package com.example.android.roomwordssample

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.roomwordssample.database.TaskRoomDatabase
import com.example.android.roomwordssample.database.dao.TaskDAO
import com.example.android.roomwordssample.database.entity.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var taskDao: TaskDAO
    private lateinit var db: TaskRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the process is killed.
        db = Room.inMemoryDatabaseBuilder(context, TaskRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        taskDao = db.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTask() = runBlocking {
        val task = Task(1, "TaskName", "TaskDetail", "2023-10-24", "10:00 AM", 60, "ONCE", false)
        taskDao.addTask(task)
        val tasksOfToday = taskDao.getTasksOfToday().first()
        assertEquals(tasksOfToday[0], task)
    }

    @Test
    @Throws(Exception::class)
    fun getAllTasks() = runBlocking {
        val task1 = Task(1, "Task1", "Detail1", "2023-10-24", "10:00 AM", 60, "ONCE", false)
        val task2 = Task(2, "Task2", "Detail2", "2023-10-25", "11:00 AM", 45, "DAILY", false)

        taskDao.addTask(task1)
        taskDao.addTask(task2)

        val allTasks = taskDao.getAllTasks().first()
        assertEquals(allTasks[0], task1)
        assertEquals(allTasks[1], task2)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllTasks() = runBlocking {
        val task1 = Task(1, "Task1", "Detail1", "2023-10-24", "10:00 AM", 60, "ONCE", false)
        val task2 = Task(2, "Task2", "Detail2", "2023-10-25", "11:00 AM", 45, "DAILY", false)

        taskDao.addTask(task1)
        taskDao.addTask(task2)

        taskDao.deleteAllTasks()
        val allTasks = taskDao.getAllTasks().first()
        assertTrue(allTasks.isEmpty())
    }
}
