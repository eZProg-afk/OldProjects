package spiral.bit.dev.moodvoice.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import kotlinx.android.synthetic.main.activity_prediction.*
import spiral.bit.dev.moodvoice.R
import spiral.bit.dev.moodvoice.other.Utils
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.random.nextInt

class PredictionActivity : AppCompatActivity() {

    lateinit var mainPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction)

        mainPrefs = this.getSharedPreferences("main_prefs", 0)
        val period = mainPrefs.getInt("period_pushes", 14400000)
        val morningPushesHour = mainPrefs.getInt("hour_pushes", 9)
        val morningPushesMinutes = mainPrefs.getInt("minutes_pushes", 0)
        val mood = mainPrefs.getString("moodForToday", "Happiness")
        when (mood) {
            "Happiness" -> {
                result_label.text = "Сегодня ты явно на позитиве, так держать!"
                mainPrefs.edit().putString("moodForToday", "Happiness").apply()
            }
            "Neutral" -> {
               val moodNeutral = Random.nextInt((1..2))
                if (moodNeutral == 1) {
                    result_label.text = "Ты сегодня нейтрален, похоже ты словил Дзен!"
                    mainPrefs.edit().putString("moodForToday", "Neutral").apply()
                }
                 else if (moodNeutral == 2) {
                     result_label.text = "Сегодня ты явно на позитиве, так держать!"
                    mainPrefs.edit().putString("moodForToday", "Happiness").apply()
                }
            }
            "Sad" -> {
                result_label.text = "Напала грусть? Выше нос, кексик, все будет ОК!"
                mainPrefs.edit().putString("moodForToday", "Sad").apply()
            }
            "Anger" -> {
                result_label.text = "Ух, кто-то не в духе… Буду аккуратней с фразочками!"
                mainPrefs.edit().putString("moodForToday", "Anger").apply()
            }
            "Fear" -> {
                result_label.text = "Ух, кто-то не в духе… Буду аккуратней с фразочками!"
                mainPrefs.edit().putString("moodForToday", "Fear").apply()
            }
        }
        //каждые 4 часа лаунчим уведомление
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, morningPushesHour)
        calendar.set(Calendar.MINUTE, morningPushesMinutes)
        calendar.set(Calendar.SECOND, 0)
        val intent = Intent(this@PredictionActivity, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val hoursToMillis = TimeUnit.HOURS.toMillis(period.toLong())
        alarmManager.setRepeating(
            AlarmManager.RTC,
            calendar.timeInMillis,
            hoursToMillis,
            pendingIntent
        )
        Log.d("TAGCHECKALARM", "onCreate:  period - $hoursToMillis   hours - $morningPushesHour, - minutes - $morningPushesMinutes")
        btn_confirm.setOnClickListener { openPredictionDialog() }
    }

    private fun openPredictionDialog() { //функция для показа дилогового окна с предсказанием
        val builder = AlertDialog.Builder(this)
        val view: View = LayoutInflater.from(this)
            .inflate(
                R.layout.dialog_prediction,
                findViewById(R.id.layout_dialog_prediction_container)
            )
        builder.setView(view)
        val dialogPrediction = builder.create()
        if (dialogPrediction.window != null) dialogPrediction.window?.setBackgroundDrawable(
            ColorDrawable(0)
        )
        val predictionTv = view.findViewById<TextView>(R.id.prediction_tv)
        predictionTv.text = Utils.predictionsList[Random.nextInt(Utils.predictionsList.size)]
        val btnLessGo = view.findViewById<TextView>(R.id.btn_less_go)
        btnLessGo.setOnClickListener {
            dialogPrediction.dismiss()
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        dialogPrediction.show()
    }
}