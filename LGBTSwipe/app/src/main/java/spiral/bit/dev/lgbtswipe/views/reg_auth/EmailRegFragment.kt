package spiral.bit.dev.lgbtswipe.views.reg_auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentEmailRegBinding
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.BaseActivity
import javax.inject.Inject

@AndroidEntryPoint
class EmailRegFragment : Fragment(R.layout.fragment_email_reg) {

    @Inject
    lateinit var mainPrefs: SharedPreferences

    @Inject
    lateinit var authClient: FirebaseAuth

    @Inject
    lateinit var dbInstance: FirebaseDatabase

    @Inject
    lateinit var storageInstance: FirebaseStorage

    private val emailBinding: FragmentEmailRegBinding by viewBinding(FragmentEmailRegBinding::bind)
    private lateinit var dataReference: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailBinding.toggleBtn.setOnClickListener { changeFragments(GenderFragment(), false) }

        val isLoginMode = arguments?.getBoolean("isLoginMode", false) == true
        if (isLoginMode) {
            emailBinding.regEmailLabel.text = getString(R.string.login_in_account_email_label)
            emailBinding.regBtn.text = "Войти"
            emailBinding.textPasswordLayout.visibility = View.VISIBLE
        }

        emailBinding.regBtn.setOnClickListener {
            if (isLoginMode) {
                val email = emailBinding.emailEt.text.toString()
                val pass = emailBinding.passwordEt.text.toString()
                login(email, pass)
            } else {
                val pass: String = mainPrefs.getString("password", "")!!
                val email = emailBinding.emailEt.text.toString()
                val phone = mainPrefs.getString("phone", "")!!
                val name = mainPrefs.getString("name", "")!!
                val gender = mainPrefs.getString("gender", "")!!
                val birthDate = mainPrefs.getString("birthDate", "")!!
                val avatar = mainPrefs.getString("avatar", "")!!
                register(email, pass, phone, name, gender, birthDate, avatar, view.context)
            }
        }
    }

    private fun login(email: String, pass: String) {
        authClient.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    FancyToast.makeText(
                        context,
                        "Успешный вход!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                    startActivity(
                        Intent(
                            requireContext(),
                            BaseActivity::class.java
                        )
                    )
                } else {
                    FancyToast.makeText(
                        context,
                        it.exception.toString(),
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR,
                        false
                    ).show()
                }
            }
    }

    private fun register(
        email: String, pass: String,
        phone: String, name: String, gender: String, birthDate: String,
        avatar: String, context: Context
    ) {
        authClient.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    val uid = authClient.uid.toString()
                    val dateMap = mutableMapOf<String, Any>()

                    REFERENCE_STORAGE = storageInstance.reference
                    CURRENT_USER_ID = uid

                    dateMap[CHILD_ID] = uid
                    dateMap[CHILD_EMAIL] = email
                    dateMap[CHILD_PASSWORD] = pass
                    dateMap[CHILD_PHONE] = phone
                    dateMap[CHILD_GENDER_MOON] = name
                    dateMap[CHILD_GENDER_MOON] = gender
                    dateMap[CHILD_BIRTH_DATE] = birthDate
                    dateMap[CHILD_PHOTO_URL_AVATAR] = avatar
                    dateMap[CHILD_NAME] = name

                    val path = REFERENCE_STORAGE.child(AVATARS_USERS)
                        .child(CURRENT_USER_ID)

                    putFileInStorage(Uri.parse(avatar), path) {
                        getURL(path) {
                            putUrlInDatabase(it) {
                                Toast.makeText(context, "SUCCESS", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    dataReference = dbInstance.reference
                    dataReference.child(NODE_USERS).child(uid)
                        .addListenerForSingleValueEvent(AppValueEventListener {
                            dataReference.child(
                                NODE_USERS
                            ).child(uid).updateChildren(dateMap)
                                .addOnSuccessListener {
                                    FancyToast.makeText(
                                        context,
                                        "Успешная регистрация!",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        false
                                    ).show()
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            BaseActivity::class.java
                                        )
                                    )
                                }
                                .addOnFailureListener { ex ->
                                    FancyToast.makeText(
                                        context,
                                        ex.message.toString(),
                                        FancyToast.LENGTH_LONG,
                                        FancyToast.ERROR,
                                        false
                                    ).show()
                                }
                        })
                } else FancyToast.makeText(
                    context,
                    it.exception?.message.toString(),
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
            }
    }
}