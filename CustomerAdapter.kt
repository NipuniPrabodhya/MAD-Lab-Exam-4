package com.falcon.todoapp

import ToDoModel
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomerAdapter(private val activity: Activity, private val context: Context, private var taskList: ArrayList<ToDoModel>) :
RecyclerView.Adapter<CustomerAdapter.MyViewHolder>(){

    // Categorization
    fun setCategorizedList(categorizedList: ArrayList<ToDoModel>) {
        taskList = categorizedList
        notifyDataSetChanged()
    }
    // Set filteredList
    fun setFilteredList(filteredList: ArrayList<ToDoModel>) {
        taskList = filteredList
        notifyDataSetChanged()
    }
    //    create new view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d("Tag", "onCreateViewHolder: ${taskList.size}")
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.my_row, parent, false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.i("Tag", "onBindViewHolder: $position");
        val currentTask = taskList[position]
        holder.task_id.text = currentTask.id.toString()
        holder.task_title.text = currentTask.title
        holder.task_description.text = currentTask.description
        holder.task_due_date.text = currentTask.dueDate
        holder.category_name.text = currentTask.category
        holder.linearLayout.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val intent = Intent(context, UpdateTask::class.java).apply {
                    putExtra("TaskID", currentTask.id)
                    putExtra("Title", currentTask.title)
                    putExtra("Description", currentTask.description)
                    putExtra("DueDate", currentTask.dueDate)
                    putExtra("Category", currentTask.category)
                }
                activity.startActivityForResult(intent, 1)
            }
        }
        holder.attachment.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val intent = Intent(context, TaskAttachment::class.java).apply {
                    putExtra("TaskID", currentTask.id)
                }
                context.startActivity(intent)
            }
        }
    }
    override fun getItemCount(): Int {
        return taskList.size
    }


    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val task_title: TextView = itemView.findViewById(R.id.taskTitle);
        val task_description: TextView = itemView.findViewById(R.id.taskDescription);
        val task_due_date: TextView = itemView.findViewById(R.id.taskDueDate);
        val task_id: TextView = itemView.findViewById(R.id.taskId);
        val category_name: TextView = itemView.findViewById(R.id.categoryView);
        val attachment: ImageButton = itemView.findViewById(R.id.attachment);
        val linearLayout: LinearLayout = itemView.findViewById(R.id.mainLayout);
    }

}