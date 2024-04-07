package spiral.bit.dev.lgbtswipe.views.reg_auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.BaseActivity
import javax.inject.Inject

@AndroidEntryPoint
class RegActivity : AppCompatActivity(R.layout.activity_hello) {

    @Inject
    lateinit var authClient: FirebaseAuth

    @Inject
    lateinit var refDb: FirebaseDatabase

    private val regBinding: RegActivity by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ACTIVITY = this
        AUTH = authClient
        USER = User()
        CURRENT_USER_ID = AUTH.uid.toString()
        REFERENCE_DATABASE = refDb.reference
        initUser {
            if (authClient.currentUser != null) startActivity(
                Intent (
                    this,
                    BaseActivity::class.java
                )
            )
            else changeFragments(fragment = GenderFragment(), addStack = false)
        }
    }
}