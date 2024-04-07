package spiral.bit.dev.guide.other

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import spiral.bit.dev.guide.models.User

lateinit var USER: User

inline fun initUser(auth: FirebaseAuth, crossinline function: () -> Unit) {
    auth.currentUser?.uid?.let {
        FirebaseDatabase.getInstance().reference.child("users").child(
            it
        ).addListenerForSingleValueEvent(AppValueEventListener { itt ->
            USER =
                itt.getValue(User::class.java)
                    ?: User(auth.currentUser?.uid.toString(), "Default User", "notgive@gmail.com",
                        "1234567890")
            if (USER.userNickName.isEmpty()) {
                USER.userNickName =
                    "Default User"
            }
            function()
        })
    }
}