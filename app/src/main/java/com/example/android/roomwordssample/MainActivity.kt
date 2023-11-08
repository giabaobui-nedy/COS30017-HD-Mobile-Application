/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.roomwordssample

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.roomwordssample.database.entity.Task
import com.example.android.roomwordssample.listapdater.TaskListAdapter
import com.example.android.roomwordssample.viewmodels.TaskViewModel
import com.example.android.roomwordssample.viewmodels.TaskViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun reportFullyDrawn() {
        super.reportFullyDrawn()
    }
    //some compliments
    private val compliments:List<String> = listOf (
        "Great job! You nailed it!",
        "Well done! Keep up the good work!",
        "You're amazing!",
        "Fantastic! You're on fire!",
        "You are on the right track!"
    )
    //viewModel for task
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as PlannerApplication).repository)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        return when (item.itemId) {
            R.id.diary -> {
                val intent = Intent(this, DiaryActivity()::class.java)
                startActivity(intent)
                true
            }
            else -> {true}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = TaskListAdapter({task -> adapterOnClickATask(task) }, {task -> adapterOnClickATaskButton(task) })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val process = findViewById<TextView>(R.id.your_progress)

        //navigate to AddTaskActivity
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
            startActivity(intent)
        }

        //if allTasks changed, update
        taskViewModel.sortedTasks.observe(this) { tasks ->
            // Update the cached copy of the words in the adapter.
            tasks?.let { adapter.submitList(it) }
            tasks?.let {
                //capture the total number of tasks
                val totalTasks = it.size
                //capture the total number of completed tasks
                val completedTasks = it.count { task -> task.isDone }
                //calculate the percentage
                val progressPercentage = (completedTasks.toFloat() / totalTasks.toFloat() * 100).toInt()
                progressBar.progress = progressPercentage
                if (progressPercentage == 100) {
                    process.text = "Well done! You have finished everything. Take a good rest!"
                } else {
                    process.text = "$progressPercentage %"
                }

            }
        }
        reportFullyDrawn()
    }

    private fun adapterOnClickATask(task: Task?) {
        val intent = Intent(this, TaskDetailActivity()::class.java)
        intent.putExtra("Task", task)
        startActivity(intent)
    }

    private fun adapterOnClickATaskButton(task: Task?) {
        task?.let {
            val compliment = compliments[getRandomNumber()]
            Toast.makeText(this, compliment, Toast.LENGTH_LONG).show()
            taskViewModel.setTaskStatus(task.id, true)
        }
    }
}

private fun getRandomNumber(): Int {
    val random = Random(System.currentTimeMillis())
    return random.nextInt(5) // Generates a random number between 0 and 4 (inclusive)
}
