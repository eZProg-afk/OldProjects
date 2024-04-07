package spiral.bit.dev.moodvoice.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projects.alshell.vokaturi.EmotionProbabilities
import com.projects.alshell.vokaturi.Vokaturi
import kotlinx.android.synthetic.main.question_layout.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import spiral.bit.dev.moodvoice.R
import spiral.bit.dev.moodvoice.other.Utils
import kotlin.random.Random

@Suppress("SENSELESS_COMPARISON")
class QuestionActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    lateinit var mainPrefs: SharedPreferences
    lateinit var vokaturiApi: Vokaturi
    lateinit var emotionFor1Question: String
    lateinit var emotionFor2Question: String
    lateinit var emotionFor3Question: String
    var counter = 0
    var isRecorded = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_layout)

        mainPrefs = this.getSharedPreferences("main_prefs", 0)

        //для начала нам нужно сгенерить 3 наших вопроса на сегодня.

        val question1 = Utils.questions[Random.nextInt(Utils.questions.size)]
        val question2 = Utils.questions[Random.nextInt(Utils.questions.size)]
        val question3 = Utils.questions[Random.nextInt(Utils.questions.size)]

        try { vokaturiApi = Vokaturi.getInstance(applicationContext)
        } catch (e: Exception) { e.printStackTrace() }

        question_label.text = question1

        record_btn.setOnClickListener {
            if (Utils.hasPermissions(this@QuestionActivity)) {
                if (record_label.text == "Запись принята") {
                    record_btn.setImageResource(R.drawable.micro_img)
                    record_label.text = "Нажми для записи"
                    time_record_label.base = SystemClock.elapsedRealtime()
                    time_record_label.stop()
                } else if (!isRecorded) {
                    record_indicator.visibility = View.VISIBLE
                    record_label.text = "Идёт запись..."
                    time_record_label.visibility = View.VISIBLE
                    time_record_label.start()
                    record_btn.setBackgroundResource(R.drawable.bg_2_btn)
                    vokaturiApi.startListeningForSpeech()
                    isRecorded = true
                } else if (isRecorded) {
                    record_label.text = "Запись принята"
                    time_record_label.base = SystemClock.elapsedRealtime()
                    time_record_label.stop()
                    record_indicator.visibility = View.GONE
                    record_btn.setBackgroundResource(R.drawable.bg_micro_btn)
                    record_btn.setImageResource(R.drawable.recorded_ic)
                    try {
                        val emotion = vokaturiApi.stopListeningAndAnalyze()
                        val capturedEmotion =
                            Vokaturi.extractEmotion(emotion)
                        when (counter) {
                            0 -> {
                                emotionFor1Question = capturedEmotion.name
                                question_label.text = question2
                            }
                            1 -> {
                                emotionFor2Question = capturedEmotion.name
                                question_label.text = question3
                            }
                            2 -> {
                                emotionFor3Question = capturedEmotion.name
                                saveEmotions()
                                startActivity(
                                    Intent(this, PredictionActivity::class.java)
                                )
                            }
                        }
                        isRecorded = false
                        counter++
                    } catch (e: Exception) {
                        Toast.makeText(this, "Слова не распознаны :(", Toast.LENGTH_SHORT).show()
                        counter--
                        isRecorded = false
                        time_record_label.stop()
                    }
                }
            } else requestPermissions() //если разрешений на запись аудио не выдано - запрашиваем
        }
    }

    private fun requestPermissions() {
        if (Utils.hasPermissions(this)) return
        EasyPermissions.requestPermissions(
            this,
            "Вам нужно принять разрешения, чтобы корректно работать с приложением!)",
            222,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun saveEmotions() {
        var resultEmotion = ""
        if (emotionFor1Question.myEquals(emotionFor1Question, emotionFor2Question)) {
            emotionFor1Question = ""
            resultEmotion = emotionFor2Question
        } else resultEmotion = emotionFor1Question
        mainPrefs.edit().putString("moodForToday", resultEmotion).apply()
    }

    private fun String.myEquals(str1: String, str2: String): Boolean {
        return this != str1 || this != str2
    }
}