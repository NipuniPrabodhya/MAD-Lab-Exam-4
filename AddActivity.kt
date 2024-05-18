package com.falcon.todoapp;

import DatabaseHandler
import ToDoModel
import android.app.Activity;
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity(){
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var dueDateInput: EditText
    private lateinit var priorityInput: EditText
    private lateinit var addBtn: Button
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private val items = arrayOf("Urgent", "Later")
    private lateinit var adapterItems: ArrayAdapter<String>
    private var item: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        autoCompleteTextView = findViewById(R.id.auto_complete_txt)
        adapterItems = ArrayAdapter(this, R.layout.list_item, items)
        autoCompleteTextView.setAdapter(adapterItems)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            item = parent.getItemAtPosition(position).toString()
            Toast.makeText(applicationContext, "Item: $item", Toast.LENGTH_SHORT).show()
        }
        titleInput = findViewById(R.id.title)
        descriptionInput = findViewById(R.id.description)
        dueDateInput = findViewById(R.id.dueDate)
        addBtn = findViewById(R.id.save)
        addBtn.setOnClickListener {
            val databaseHandler = DatabaseHandler(this)
            val task = ToDoModel()
            task.title = titleInput.text.toString().trim()
            task.description = descriptionInput.text.toString().trim()
            task.dueDate = dueDateInput.text.toString().trim()
            task.category = autoCompleteTextView.text.toString().trim()
            Log.i("print", task.title.toString().trim())
            databaseHandler.insertTask(task);
        }
    }
}
