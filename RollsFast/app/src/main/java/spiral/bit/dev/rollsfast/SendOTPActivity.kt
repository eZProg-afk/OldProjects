package spiral.bit.dev.rollsfast

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_send_otp.*
import spiral.bit.dev.rollsfast.other.CURRENT_USER_ID
import spiral.bit.dev.rollsfast.other.REFERENCE_DATABASE
import spiral.bit.dev.rollsfast.other.REFERENCE_STORAGE
import spiral.bit.dev.rollsfast.other.initUser
import java.util.concurrent.TimeUnit

class SendOTPActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var switcher = true
    private var verifyId = ""
    private var phoneNumber = ""

    private val phoneCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credentials: PhoneAuthCredential) {}

        override fun onVerificationFailed(error: FirebaseException) {
            Toast.makeText(this@SendOTPActivity, error.localizedMessage, Toast.LENGTH_SHORT)
                .show()
        }

        override fun onCodeSent(
            verificationId: String,
            forceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            verifyId = verificationId
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_otp)

        REFERENCE_DATABASE = FirebaseDatabase.getInstance().reference
        REFERENCE_STORAGE = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("ru")
        if (auth.currentUser != null) {
            CURRENT_USER_ID = auth.currentUser?.uid.toString()
            if (auth.currentUser?.uid.equals("A5NrPGIfdbhsLkeeNaEOoFogay62") ||
                auth.currentUser?.uid.equals("cq1PsIu3gjWS6DiSCrIG5zWnya43")
            ) {
                startActivity(
                    Intent(
                        this@SendOTPActivity, AdmActivity::class.java
                    )
                )
            } else {
                initUser {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                }
            }
        }
        btn_next.setOnClickListener {
            btn_next.visibility = View.GONE
            if (switcher) goToEnterCode()
            else goToEnterPhoneNumber()
        }
    }

    private fun goToEnterCode() {
        val animationOut: Animation =
            TranslateAnimation(0F, getScreenWidth() + 10F, 0F, 0F)
        animationOut.duration = 600
        animationOut.fillAfter = true
        input_mobile_phone.startAnimation(animationOut)
        enter_ur_phone_label.startAnimation(animationOut)

        Handler().postDelayed({
            val animationIn: Animation =
                TranslateAnimation(getScreenWidth() - 100F, 0F, 0F, 0F)
            animationIn.duration = 600
            animationIn.fillAfter = true
            input_mobile_phone.visibility = View.GONE
            verify_views.visibility = View.VISIBLE
            verify_views.startAnimation(animationIn)
            label_img.setImageResource(R.drawable.received)
            switcher = false
            setUpOtpInputs()
        }, 600)
        phoneNumber = input_mobile_phone.text.toString()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this@SendOTPActivity)
            .setCallbacks(phoneCallback)
            .build()
        PhoneAuthProvider
            .verifyPhoneNumber(options)
    }

    private fun goToEnterPhoneNumber() {
        val animationOut: Animation =
            TranslateAnimation(0F, getScreenWidth() + 10F, 0F, 0F)
        animationOut.duration = 600
        animationOut.fillAfter = true
        verify_views.visibility = View.GONE
        verify_views.startAnimation(animationOut)

        Handler().postDelayed({
            val animationIn: Animation =
                TranslateAnimation(getScreenWidth() - 100F, 0F, 0F, 0F)
            animationIn.duration = 600
            animationIn.fillAfter = true
            input_mobile_phone.visibility = View.VISIBLE
            input_mobile_phone.startAnimation(animationOut)
            enter_ur_phone_label.startAnimation(animationOut)
            label_img.setImageResource(R.drawable.send)
            switcher = true
        }, 600)
    }

    private fun setUpOtpInputs() {
        inputCode1.requestFocus()
        inputCode1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) inputCode2.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        inputCode2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) inputCode3.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        inputCode3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) inputCode4.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        inputCode4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) inputCode5.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        inputCode5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) inputCode6.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        inputCode6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    if (inputCode1.text.toString().trim().isEmpty() ||
                        inputCode2.text.toString().trim().isEmpty() ||
                        inputCode3.text.toString().trim().isEmpty() ||
                        inputCode4.text.toString().trim().isEmpty() ||
                        inputCode5.text.toString().trim().isEmpty() ||
                        inputCode6.text.toString().trim().isEmpty()
                    ) {
                        Toast.makeText(
                            this@SendOTPActivity,
                            "Пожалуйста, введите корректный код!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    val code = inputCode1.text.toString() + inputCode2.text.toString() +
                            inputCode3.text.toString() + inputCode4.text.toString() +
                            inputCode5.text.toString() + inputCode6.text.toString()
                    val pac = PhoneAuthProvider.getCredential(verifyId, code)
                    FirebaseAuth.getInstance().signInWithCredential(pac)
                        .addOnCompleteListener {
                            if (it.isSuccessful)
                                if (auth.currentUser?.uid.equals("A5NrPGIfdbhsLkeeNaEOoFogay62") ||
                                    auth.currentUser?.uid.equals("cq1PsIu3gjWS6DiSCrIG5zWnya43") ) {
                                    CURRENT_USER_ID = auth.currentUser?.uid.toString()
                                        startActivity(
                                        Intent(
                                            this@SendOTPActivity, AdmActivity::class.java
                                        )
                                    )
                                } else {
                                    CURRENT_USER_ID = auth.currentUser?.uid.toString()
                                    startActivity(
                                        Intent(
                                            this@SendOTPActivity, MainActivity::class.java
                                        )
                                    )
                                }
                            else Toast.makeText(
                                this@SendOTPActivity,
                                "Введённый код некорректен. \n Попробуйте ещё раз.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun getScreenWidth(): Float {
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val screenWidth: Int = point.x
        return screenWidth.toFloat()
    }
}