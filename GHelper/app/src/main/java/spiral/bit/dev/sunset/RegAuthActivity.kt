package spiral.bit.dev.sunset

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_reg_auth.*
import spiral.bit.dev.sunset.models.User
import spiral.bit.dev.sunset.other.USER
import spiral.bit.dev.sunset.other.initUser

class RegAuthActivity : AppCompatActivity() {

    private var isAuth = true
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_auth)
        supportActionBar?.title = "Регистрация"
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser?.uid != null)
            initUser(auth) { startActivity(Intent(this, MainActivity::class.java)) }

        reg_btn.setOnClickListener {
            if (!isAuth) authentication()
            else if (isAuth) registration()
        }

        already_reg_label.setOnClickListener { trigger() }
    }

    private fun trigger() {
        if (isAuth) {
            isAuth = false
            repeat_password_layout.visibility = View.GONE
            nickname_layout.visibility = View.GONE
            reg_btn.text = "Авторизация"
            already_reg_label.text = "Нет аккаунта? \n Нажмите сюда!"
        } else {
            isAuth = true
            repeat_password_layout.visibility = View.VISIBLE
            nickname_layout.visibility = View.VISIBLE
            reg_btn.text = "Регистрация"
            already_reg_label.setText(R.string.already_reg_label)
        }
    }

    private fun authentication() {
        if (email_et.text.toString()
                .isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email_et.text.toString()).matches()
        ) {
            if (password_et.text?.length!! >= 7) {
                auth.signInWithEmailAndPassword(
                    email_et.text.toString(), password_et.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Успешный вход!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        initUser(auth) { startActivity(intent) }
                    } else Toast.makeText(
                        this,
                        "Ошибка:( ${it.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else Toast.makeText(this, "Пароли должны совпадать!", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(
            this,
            "Пароль должен состоять из 7 или более символов",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun registration() {
        if (email_et.text?.isNotEmpty() == true && nickname_et.text?.isNotEmpty() == true) {
            if (password_et.text?.length!! >= 7) {
                if (password_et.text.toString() == password_repeat_et.text.toString()) {
                    auth.createUserWithEmailAndPassword(
                        email_et.text.toString(), password_et.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Успешная регистрация!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            USER = User(
                                auth.currentUser?.uid,
                                nickname_et.text.toString(),
                                email_et.text.toString(),
                                password_et.text.toString()
                            )
                            FirebaseDatabase.getInstance().reference.child("users")
                                .child(auth.currentUser?.uid.toString())
                                .setValue(USER)
                                .addOnSuccessListener {
                                    startActivity(intent)
                                }
                                .addOnFailureListener { er ->
                                    Toast.makeText(
                                        this,
                                        "Ошибка:( ${er.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else Toast.makeText(
                            this,
                            "Ошибка:( ${it.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else Toast.makeText(this, "Пароли должны совпадать!", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(
                this,
                "Пароль должен состоять из 7 или более символов",
                Toast.LENGTH_SHORT
            ).show()
        } else Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
    }
}