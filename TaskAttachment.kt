package com.falcon.todoapp;

import DatabaseHandler
import ToDoModel
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class TaskAttachment: AppCompatActivity(){
    private lateinit var attachment: Button
    private lateinit var select: Button
    private lateinit var imageView: ImageView
    private var imageUrl: Uri? = null
    private var imageToStore: Bitmap? = null
    private val databaseHandler: DatabaseHandler by lazy { DatabaseHandler(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_attachment)
        attachment = findViewById(R.id.imageSave)
        imageView = findViewById(R.id.imageView)
        attachment.setOnClickListener {
            try {
                if (imageView.drawable != null) {
                    val task = ToDoModel().apply {
                        val extras = intent.extras
                        id = extras?.getInt("TaskID") ?: 0
                        img = imageToStore
                    }
                    databaseHandler.uploadImg(task)
                }
            } catch (e: Exception) {
                Toast.makeText(this@TaskAttachment, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun chooseImage(objView: View) {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUrl = data.data
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(contentResolver, imageUrl)
                imageView.setImageBitmap(imageToStore)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
