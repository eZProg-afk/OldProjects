package spiral.bit.dev.moodvoice.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import spiral.bit.dev.moodvoice.R

class SignUpActivity : AppCompatActivity() {

    private lateinit var mainPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mainPrefs = this.getSharedPreferences("main_prefs", 0)
        //иначе, если это первый запуск приложения
        Log.d("entered ELSE BLOCK", "onCreate: ")
        btn_confirm.setOnClickListener {
            if (et_name.text.isNotEmpty()) {
                val name = et_name.text.toString()
                mainPrefs.edit().putString("userName", name).apply() //сохраняем имя юзера
                mainPrefs.edit().putString("isFirstLaunch", "not")
                    .apply() // сохраняем то, что первый запуск уже был
                startActivity(
                    Intent(
                        this,
                        GoodMorningActivity::class.java
                    )
                ) //идём далее зареганными
            } else Toast.makeText(this, "Пожалуйста, введите имя !)", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() { }
}