package spiral.bit.dev.sunset

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import me.ibrahimsn.lib.OnItemSelectedListener
import spiral.bit.dev.sunset.fragments.GroupsFragment
import spiral.bit.dev.sunset.fragments.ProfileFragment
import spiral.bit.dev.sunset.fragments.SearchFragment
import spiral.bit.dev.sunset.other.ACTIVITY
import spiral.bit.dev.sunset.other.changeFragments

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ACTIVITY = this

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, ProfileFragment())
                .commit()
        }

        bottomBar.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int): Boolean {
                when (pos) {
                    0 -> changeFragments(ProfileFragment(), true)
                    1 -> changeFragments(GroupsFragment(), true)
                    2 -> changeFragments(SearchFragment(), true)
                    else -> changeFragments(ProfileFragment(), true)
                }
                return true
            }
        }
    }
}