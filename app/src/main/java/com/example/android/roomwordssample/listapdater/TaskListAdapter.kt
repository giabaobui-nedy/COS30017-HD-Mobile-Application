package com.example.android.roomwordssample.listapdater

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.roomwordssample.R
import com.example.android.roomwordssample.database.entity.Task

class TaskListAdapter(private val onClickOnTask: (Task?) -> Unit, private val onClickOnDoneButton: (Task?) -> Unit)
    : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(WORDS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.create(parent, onClickOnTask, onClickOnDoneButton)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskViewHolder(taskView: View , val onClickOnTask: (Task?) -> Unit, val onClickOnDoneButton: (Task?) -> Unit) : RecyclerView.ViewHolder(taskView) {
        private val time: TextView = taskView.findViewById(R.id.time)
        private val name: TextView = taskView.findViewById(R.id.name)
        private val done: ImageButton = taskView.findViewById(R.id.done)
        private val theTaskView: View = taskView

        init {
            Log.i("DEBUG", "a new holder is created")
        }


        fun bind(task: Task) {
            time.text = task.time
            name.text = task.name
            done.isEnabled = !task.isDone

            name.setOnClickListener {
                onClickOnTask(task)
            }
            done.setOnClickListener {
                onClickOnDoneButton(task)
            }

            // Check if the task is done and apply strike-through if it is
            if (task.isDone) {
                name.paintFlags = name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                time.alpha = 0.1f
                name.alpha = 0.1f
                done.alpha = 0.1f
                done.elevation = 0f
                theTaskView.elevation = 0f
            } else {
                name.paintFlags = name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                time.alpha = 1.0f
                name.alpha = 1.0f
                done.alpha = 1.0f
                done.elevation = 10f
                theTaskView.elevation = 5f
            }
        }

        companion object {
            fun create(parent: ViewGroup, onClickOnTask: (Task?) -> Unit, onClickOnDoneButton: (Task?) -> Unit): TaskViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.task_view, parent, false)
                return TaskViewHolder(view, onClickOnTask, onClickOnDoneButton)
            }
        }
    }

    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }
}