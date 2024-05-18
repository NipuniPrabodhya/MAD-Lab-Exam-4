import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.falcon.todoapp.R

class NotificationBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val builder = NotificationCompat.Builder(context, "NotifyMe")
            .setSmallIcon(R.drawable.ic_baseline_addmark)
            .setContentTitle("Reminder ToDo Application")
            .setContentText("Activity needs to be finished")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is not granted, we cannot proceed with the notification
            return
        }
        notificationManagerCompat.notify(200, builder.build())
    }
}
