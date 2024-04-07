package spiral.bit.dev.lgbtswipe.views.main

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.ActivityBaseBinding
import spiral.bit.dev.lgbtswipe.databinding.BottomNavMenuBinding
import spiral.bit.dev.lgbtswipe.other.*
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class BaseActivity : AppCompatActivity(R.layout.activity_base) {

    @Inject
    lateinit var storageInstance: FirebaseStorage

    @Inject
    lateinit var authClient: FirebaseAuth

    @Inject
    lateinit var networkConnection: NetworkConnection

    private val baseBinding: ActivityBaseBinding by viewBinding()

    override fun onResume() {
        super.onResume()
        UserStatus.updateState(UserStatus.ONLINE)
    }

    override fun onPause() {
        super.onPause()
        UserStatus.updateState(UserStatus.OFFLINE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MAIN_ACTIVITY = this
        REFERENCE_STORAGE = storageInstance.reference
        CURRENT_USER_ID = authClient.uid.toString()
        initUser {}
        UserStatus.updateState(UserStatus.ONLINE)
        networkConnection.observe(this, { isConnected ->
            if (!isConnected) showNoInternetDialog()
        })
        changeMainFragments(CardsFragment(), false)
        setUpBottomNavView()
    }

    private fun showNoInternetDialog() {
        val builder = AlertDialog.Builder(this)
        val view: View = LayoutInflater.from(this)
            .inflate(
                R.layout.dialog_no_internet,
                findViewById(R.id.dialog_no_internet_container)
            )
        builder.setView(view)
        val noInternetDialog = builder.create()
        if (noInternetDialog.window != null) {
            noInternetDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        view.findViewById<LottieAnimationView>(R.id.anim_no_internet).playAnimation()
        view.findViewById<View>(R.id.text_reboot).setOnClickListener {
            noInternetDialog.dismiss()
            val mStartActivity = Intent(this, BaseActivity::class.java)
            val mPendingIntentId = 123456
            val mPendingIntent = PendingIntent.getActivity(
                this,
                mPendingIntentId,
                mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val amr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            amr.set(
                AlarmManager.RTC, System.currentTimeMillis() + 100,
                mPendingIntent
            )
            exitProcess(0)
        }
        view.findViewById<View>(R.id.text_exit).setOnClickListener { noInternetDialog.dismiss() }
        noInternetDialog.show()
    }

    @SuppressLint("NonConstantResourceId")
    fun setUpBottomNavView() {
        baseBinding.bottomMenu.fabHome.setOnClickListener {
            changeMainFragments(
                CardsFragment(),
                false
            )
        }

        baseBinding.bottomMenu.bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home_item -> {
                    changeMainFragments(CardsFragment(), true)
                }
                R.id.profile_item -> {
                    changeMainFragments(ProfileFragment(USER), true)
                }
                R.id.messages_item -> {
                    changeMainFragments(ChatsFragment(), true)
                }
            }
            false
        }

    }

    fun hideMenu(isVisible: Boolean) {
        if (isVisible) baseBinding.bottomMenu.bottomNavigationView.visibility = View.VISIBLE
        else baseBinding.bottomMenu.bottomNavigationView.visibility = View.GONE
    }
}