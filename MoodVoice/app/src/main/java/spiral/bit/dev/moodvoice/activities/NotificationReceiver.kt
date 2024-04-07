package spiral.bit.dev.moodvoice.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import spiral.bit.dev.moodvoice.R
import spiral.bit.dev.moodvoice.other.Utils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class NotificationReceiver : BroadcastReceiver() {

    private lateinit var prefs: SharedPreferences
    private var countAlarmTriggered = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        prefs = context?.getSharedPreferences("main_prefs", 0)!!
        val randomWish = when (prefs.getString("moodForToday", "Happiness")) {
            "Happiness" -> {
                Utils.goodMotivations[Random.nextInt(Utils.neutralMotivations.size)]
            }
            "Neutral" -> {
                Utils.neutralMotivations[Random.nextInt(Utils.neutralMotivations.size)]
            }
            "Sad" -> {
                Utils.sadnessMotivations[Random.nextInt(Utils.sadnessMotivations.size)]
            }
            "Fear" -> {
                Utils.fearMotivations[Random.nextInt(Utils.fearMotivations.size)]
            }
            "Anger" -> {
                Utils.angryMotivations[Random.nextInt(Utils.angryMotivations.size)]
            }
            else -> Utils.angryMotivations[Random.nextInt(Utils.fearMotivations.size)]
        }
        if (checkIsNot22AndLater()) {
            Log.d("TAGCHECKALARM", "onReceive:    $countAlarmTriggered")
            showNotification(randomWish, context)
        }
    }

    private fun checkIsNot22AndLater(): Boolean {
        try {
            val string1 = "22:00:00"
            val time1 = SimpleDateFormat("HH:mm:ss").parse(string1)
            val calendar1 = Calendar.getInstance()
            calendar1.time = time1
            calendar1.add(Calendar.DATE, 1)
            val string2 = "09:00:00"
            val time2 = SimpleDateFormat("HH:mm:ss").parse(string2)
            val calendar2 = Calendar.getInstance()
            calendar2.time = time2
            calendar2.add(Calendar.DATE, 1)
            val someRandomTime = "01:00:00"
            val d = SimpleDateFormat("HH:mm:ss").parse(someRandomTime)
            val calendar3 = Calendar.getInstance()
            calendar3.time = d
            calendar3.add(Calendar.DATE, 1)
            val x = calendar3.time
            if (x.after(calendar1.time) && x.before(calendar2.time)) {
                //проверяем текущее время, является ли оно между 22:00 и 09:00 (22 и позже)
                return true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }


    private fun showNotification(nameOfNote: String?, context: Context) {
        createNotificationChannel(context)
        val notificationIntent = Intent(context, GoodMorningActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            context,
            0, notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val builder = NotificationCompat.Builder(
            context,
            "channel_id_daily"
        )
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle("Улыбнись")
            .setContentText(nameOfNote)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentIntent)
            .setStyle(NotificationCompat.BigTextStyle())
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.app_icon
                )
            )
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(
            222333,
            builder.build()
        )
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "DAILY"
            val description = "MOOD"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                "channel_id_daily",
                name,
                importance
            )
            channel.description = description
            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}
