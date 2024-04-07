package spiral.bit.dev.moodvoice.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.good_morning.*
import spiral.bit.dev.moodvoice.R

class GoodMorningActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this.getSharedPreferences("main_prefs", 0)
                .getString("isFirstLaunch", "").equals("")) { //если запуск не первый, то:
            //идём на экран Good Morning
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        setContentView(R.layout.good_morning)

        val userName = this.getSharedPreferences("main_prefs", 0)
            .getString("userName", "Пользователь") //вытаскиваем наше имя
        hello_user_label.text = "Доброе утро, $userName!" //присваиваем текстовому полю имя юзера

        //по нажатию кнопки "Поехали!" идём на экран с вопросами
        btn_next.setOnClickListener { startActivity(Intent(this, QuestionActivity::class.java)) }
        //Тут переходим по нажатию на иконку настроек в экран настроек
        settings_img.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
    }
}