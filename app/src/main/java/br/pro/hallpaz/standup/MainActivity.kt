package br.pro.hallpaz.standup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private var notificationManager: NotificationManager? = null
    private val notificationBuilder: NotificationCompat.Builder
    get() {
        val mainIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                                                        NOTIFICATION_ID,
                                                        mainIntent,
                                                        PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("Alerta de PÉ")
            .setContentText("Levanta e voa!")
            .setSmallIcon(R.drawable.ic_thumb_up)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH) //compatibility down here
            .setDefaults(NotificationCompat.DEFAULT_ALL)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createNotificationChannel()
        setUpListeners()
    }

    private fun setUpListeners(){
        toggle_alarm_button.setOnCheckedChangeListener { compoundButton, checked ->
            val toastMessage = if (checked) {
                deliverNotification(this)
                getString(R.string.alarm_on_string)
            } else{
                notificationManager?.cancelAll()
                getString(R.string.alarm_off_string)
            }

            Toast.makeText(this@MainActivity, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNotificationChannel(){
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID,
                                                            "Pra que serve esse nome?",
                                                            NotificationManager.IMPORTANCE_HIGH).apply {
                enableLights(true)
                lightColor = Color.MAGENTA
                enableVibration(true)
                description = "Notifica a cada 15 minutos para se levantar"
            }

            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    fun deliverNotification(context: Context){
        val builder = notificationBuilder
        notificationManager?.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        const val PRIMARY_CHANNEL_ID = "primary_channel_id"
        const val NOTIFICATION_ID = 7
    }


}
