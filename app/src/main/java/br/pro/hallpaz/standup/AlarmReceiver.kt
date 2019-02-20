package br.pro.hallpaz.standup

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManager? = null

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        deliverNotification(context)

    }

    private fun deliverNotification(context: Context){
        val mainIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,
                                                        MainActivity.NOTIFICATION_ID,
                                                        mainIntent,
                                                        PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context, MainActivity.PRIMARY_CHANNEL_ID)
            .setContentTitle("Alerta de PÉ")
            .setContentText("Levanta e voa!")
            .setSmallIcon(R.drawable.ic_thumb_up)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH) //compatibility down here
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        notificationManager?.notify(MainActivity.NOTIFICATION_ID, notificationBuilder.build())
    }
}

