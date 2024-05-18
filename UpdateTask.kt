package com.falcon.todoapp;

import DatabaseHandler
import ToDoModel
import android.app.Activity;
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateTask : AppCompatActivity(){
    private lateinit var task_title: EditText
    private lateinit var task_description: EditText
    private lateinit var task_due_date: EditText
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var autoCompleteTextView1: AutoCompleteTextView
    private lateinit var adapterItems1: ArrayAdapter<String>
    private lateinit var item1: String
    private val items1 = arrayOf("Urgent", "Later")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        autoCompleteTextView1 = findViewById(R.id.auto_complete_txt1)
        adapterItems1 = ArrayAdapter(this, R.layout.list_item, items1)
        autoCompleteTextView1.setAdapter(adapterItems1)
        autoCompleteTextView1.setOnItemClickListener { parent, view, position, id ->
            item1 = parent.getItemAtPosition(position).toString()
            Toast.makeText(applicationContext, "Item:$item1", Toast.LENGTH_SHORT).show()
        }
        task_title = findViewById(R.id.title2)
        task_description = findViewById(R.id.description2)
        task_due_date = findViewById(R.id.dueDate2)
        editBtn = findViewById(R.id.edit)
        deleteBtn = findViewById(R.id.delete)
        val extras = intent.extras
        task_title.setText(extras?.getString("Title"))
        task_description.setText(extras?.getString("Description"))
        task_due_date.setText(extras?.getString("DueDate"))
        autoCompleteTextView1.setText(extras?.getString("Category"))
        editBtn.setOnClickListener {
            val databaseHandler = DatabaseHandler(this@UpdateTask)
            val task = ToDoModel().apply {
// Assuming you want to get the ID from the extras (you need to set it in the intent previously)
                id = extras?.getInt("TaskID") ?: 0
                title = task_title.text.toString().trim()
                description = task_description.text.toString().trim()
                dueDate = task_due_date.text.toString().trim()
                category = autoCompleteTextView1.text.toString().trim()
            }
            databaseHandler.updateTaskDetail(task)
// Feedback for the user
            Toast.makeText(this@UpdateTask, "Task updated!", Toast.LENGTH_SHORT).show()
// Finish the activity (you might also want to refresh the list in the previous activity)
            finish()
        }
        deleteBtn.setOnClickListener {
            val databaseHandler = DatabaseHandler(this@UpdateTask)
            val task = ToDoModel().apply {
// Assuming you want to get the ID from the extras (you need to set it in the intent previously)
                id = extras?.getInt("TaskID") ?: 0
            }
            databaseHandler.deleteTaskDetail(task.id)
// Finish the activity (you might also want to refresh the list in the previous activity)
            finish()
        }
    }

}
