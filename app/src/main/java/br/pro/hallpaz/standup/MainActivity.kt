package br.pro.hallpaz.standup

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private var notificationManager: NotificationManager? = null
    private var alarmManager: AlarmManager? = null
    private val repeatInterval: Long
        get() = 1000//AlarmManager.INTERVAL_FIFTEEN_MINUTES

    private val triggerTime: Long
        get() = SystemClock.elapsedRealtime() + repeatInterval

    private val notifyPendingIntent: PendingIntent
    get() = PendingIntent.getBroadcast(this,
                                            NOTIFICATION_ID,
                                            Intent(this, AlarmReceiver::class.java),
                                            PendingIntent.FLAG_UPDATE_CURRENT)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createNotificationChannel()
        setUpListeners()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    }

    private fun setUpListeners(){
        toggle_alarm_button.setOnCheckedChangeListener { compoundButton, checked ->
            val toastMessage = if (checked) {
                alarmManager?.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                                    triggerTime,
                                                    repeatInterval,
                                                    notifyPendingIntent)
                getString(R.string.alarm_on_string)
            } else{
                alarmManager?.cancel(notifyPendingIntent)
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

    companion object {
        const val PRIMARY_CHANNEL_ID = "primary_channel_id"
        const val NOTIFICATION_ID = 7
    }


}
