package com.falcon.todoapp;

import DatabaseHandler
import DialogCloseListener
import NotificationBroadcast
import ToDoModel
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), DialogCloseListener {
    private lateinit var taskReclerView : RecyclerView;
    private lateinit var customerAdapter: CustomerAdapter
    private var taskList: ArrayList<ToDoModel> = ArrayList()
    private lateinit var fab: FloatingActionButton
    private lateinit var db: DatabaseHandler
    private lateinit var searchView: SearchView
    private lateinit var allBtn: Button
    private lateinit var category1: Button
    private lateinit var category2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = DatabaseHandler(this);
        db.openDatabase();
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!newText?.isEmpty()!!){
                    filterList(newText);
                }
                else{
                    viewAll();
                }
                return true;
            }
        })

        allBtn = findViewById(R.id.btnall);
        category1 = findViewById(R.id.btncat1);
        category2 = findViewById(R.id.btncat2);

        allBtn.setOnClickListener {
            categorize("all");
        }

        category1.setOnClickListener {
            categorize("Category1");
        }

        category2.setOnClickListener {
            categorize("Category2");
        }

        taskReclerView = findViewById(R.id.tasksRecyclerView);
        taskReclerView.layoutManager = LinearLayoutManager(this);
        val imgBtn = findViewById<ImageView>(R.id.faqBtn);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener{
            val intent = Intent(this@MainActivity, AddActivity::class.java);
            startActivity(intent);
        }

        imgBtn.setOnClickListener{
            val intent = Intent(this@MainActivity, FaqActivity::class.java);
            startActivity(intent);
        }

        viewAll();

        customerAdapter = CustomerAdapter(this@MainActivity, this, taskList);
        taskReclerView.adapter = customerAdapter;
        taskReclerView.layoutManager = LinearLayoutManager(this);

    }

    private fun categorize(text: String) {
        val categorizedList = ArrayList<ToDoModel>()
        if (text == "all") {
            categorizedList.addAll(taskList)
        }
        if (text == "Category1") {
            for (task in taskList) {
                if (task.category?.contains("Later") == true) {
                    categorizedList.add(task)
                }
            }
        }
        if (text == "Category2") {
            for (task in taskList) {
                if (task.category?.contains("Urgent") == true) {
                    categorizedList.add(task)
                }
            }
        }
        customerAdapter.setCategorizedList(categorizedList)
    }

    // FilterList
    private fun filterList(text: String) {
        val filteredList = ArrayList<ToDoModel>()
        for (task in taskList) {
            if (task.title?.toLowerCase(Locale.getDefault())?.contains(text.toLowerCase(Locale.getDefault())) == true) {
                filteredList.add(task)
            }
            if (task.dueDate?.contains(text) == true) {
                filteredList.add(task)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found!", Toast.LENGTH_SHORT).show()
        } else {
            customerAdapter.setFilteredList(filteredList)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            recreate()
        }
    }

    // Display all tasks
    private fun viewAll() {
        val cursor: Cursor? = db.readAllData()
        if (cursor != null) {
            if (cursor.count == 0) {
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
            } else {
                while (cursor.moveToNext()) {
                    val task = ToDoModel().apply {
                        id = cursor.getString(0).toInt()
                        title = cursor.getString(1)
                        description = cursor.getString(2)
                        dueDate = cursor.getString(3)
                        category = cursor.getString(4)
                    }
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    try {
                        val date: Date = format.parse(task.dueDate)
                        val calendar: Calendar = Calendar.getInstance()
                        val year: Int = calendar.get(Calendar.YEAR)
                        val month: Int = calendar.get(Calendar.MONTH) + 1 // Month is 0-based, so add 1
                        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
                        val today: String = if (month < 10) "$year-0$month-$day" else "$year-$month-$day"
                        if (task.dueDate == today) {
                            createNotification()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    Log.i("print-mainActivity", task.title.toString());
                    taskList.add(task)
                }
            }
        }
    }

    override fun handleDialogClose(dialog: DialogInterface) {
        taskList.reverse()
    }

    // Notification channel creation
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ToDoReminderChannel"
            val descriptionText = "Channel for ToDo Reminder"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("NotifyMe", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Notification creation
    private fun createNotification() {
        createNotificationChannel()
        val intent = Intent(this@MainActivity, NotificationBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent)
    }
}

