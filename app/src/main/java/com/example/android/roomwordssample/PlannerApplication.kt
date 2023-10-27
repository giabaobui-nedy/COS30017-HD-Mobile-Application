package com.example.android.roomwordssample

import android.app.Application
import com.example.android.roomwordssample.database.TaskRoomDatabase
import com.example.android.roomwordssample.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PlannerApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { TaskRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TaskRepository(database.taskDao()) }

}
