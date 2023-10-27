package com.example.android.roomwordssample.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.roomwordssample.database.dao.TaskDAO
import com.example.android.roomwordssample.database.entity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Task::class], version = 1, exportSchema = false)
//@TypeConverters(DateConverter::class)
abstract class TaskRoomDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TaskRoomDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope): TaskRoomDatabase  {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskRoomDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class TaskDatabaseCallback (
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                TaskRoomDatabase.INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.taskDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(taskDao: TaskDAO) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            taskDao.deleteAllTasks()

            val task1 = Task(1, "Task 1", "Do something important", "2023-10-24", "08:00 AM", 60, "Once", false)
            val task2 = Task(2, "Task 2", "Meet with a friend", "2023-10-25", "05:30 PM", 90, "Daily", false)
            val task3 = Task(3, "Task 3", "Read a book", "2023-10-26", "10:30 AM", 120, "Daily", false)
            val task4 = Task(4, "Task 4", "Go for a run", "2023-10-24", "06:00 AM", 30, "Once", true)

            taskDao.addTask(task1)
            taskDao.addTask(task2)
            taskDao.addTask(task3)
            taskDao.addTask(task4)
        }
    }
}