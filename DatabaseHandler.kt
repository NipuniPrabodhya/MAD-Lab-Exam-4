import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayOutputStream
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "toDoListDatabase"
        private const val TODO_TABLE = "todo"
        private const val ID = "id"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val DUE_DATE = "due_date"
        private const val CATEGORY = "category"
        private const val IMG = "image"
    }
    private val context: Context = context.applicationContext
    private var db: SQLiteDatabase? = null
    private var byteArrayOutputStream: ByteArrayOutputStream? = null
    private var imgInByte: ByteArray? = null
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TODO_TABLE = ("CREATE TABLE $TODO_TABLE($ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$TITLE TEXT, $DESCRIPTION TEXT, $DUE_DATE TEXT, $CATEGORY TEXT, $IMG BLOB)")
        db.execSQL(CREATE_TODO_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TODO_TABLE")
        onCreate(db)
    }
    fun openDatabase() {
        db = this.writableDatabase
    }
    fun insertTask(task: ToDoModel) {
        openDatabase();
        val cv = ContentValues()
        cv.put(TITLE, task.title)
        cv.put(DESCRIPTION, task.description)
        cv.put(DUE_DATE, task.dueDate)
        cv.put(CATEGORY, task.category)
        Log.i("tag", "insertTask: ${cv.get("title")}");
        val result = db?.insert(TODO_TABLE, null, cv);
        Log.i("tag", "result: ${result}");
        if (result == -1L) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show()
            println(task.category)
        }
    }
    fun readAllData(): Cursor? {
        val query = "SELECT * FROM $TODO_TABLE"
        val db = this.readableDatabase
        return db.rawQuery(query, null)
    }
    fun updateTaskDetail(toDoModel: ToDoModel) {
        openDatabase();
        val cv = ContentValues()
        cv.put(TITLE, toDoModel.title)
        cv.put(DESCRIPTION, toDoModel.description)
        cv.put(DUE_DATE, toDoModel.dueDate)
        val result = db?.update(TODO_TABLE, cv, "id=?", arrayOf(toDoModel.id.toString()))
        if (result == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show()
        }
    }
    fun uploadImg(toDoModel: ToDoModel) {
        val cv = ContentValues()
        val img = toDoModel.img
        byteArrayOutputStream = ByteArrayOutputStream()
        img?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream!!)
        imgInByte = byteArrayOutputStream?.toByteArray()
        cv.put(IMG, imgInByte)
        val result = db?.update(TODO_TABLE, cv, "id=?", arrayOf(toDoModel.id.toString()))
        if (result == -1) {
            Toast.makeText(context, "Failed to Attach", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Attach Successfully", Toast.LENGTH_SHORT).show()
        }
    }
    fun deleteTaskDetail(taskId: Int) {
        openDatabase();
        val result = db?.delete(TODO_TABLE, "id=?", arrayOf(taskId.toString()))
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show()
        }
    }
    fun updateStatus(id: Int, status: Int) {
        val cv = ContentValues()
        cv.put(DESCRIPTION, status)
        db?.update(TODO_TABLE, cv, "$ID=?", arrayOf(id.toString()))
    }
    fun updateTask(id: Int, task: String) {
        val cv = ContentValues()
        cv.put(TITLE, task)
        db?.update(TODO_TABLE, cv, "$ID=?", arrayOf(id.toString()))
    }
    fun deleteTask(id: Int) {
        db?.delete(TODO_TABLE, "$ID=?", arrayOf(id.toString()))
    }
}