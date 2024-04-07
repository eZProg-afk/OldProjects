package spiral.bit.dev.sunset.other

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import pub.devrel.easypermissions.EasyPermissions
import spiral.bit.dev.sunset.MainActivity
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.models.User
import java.io.File

lateinit var ACTIVITY: MainActivity
lateinit var USER: User
const val MSG_TYPE_RECEIVED = 0
const val MSG_TYPE_USER = 1

class Constants {
    companion object {
        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = "AAAA_tg8r64:APA91bHtBJF5Mq6rum1dMEeV1_fT7IobeSIYwNCx8vdj6etxUi3EPiU9tjuGEBegeGvHzQu1djUq8Jls8yL7xF-j4Rim5LbCnvQZufoH3vqXeYYG95AnsondSgm4zA_752LF12cppIvC"
        const val SENDER_ID = "1094549548974"
        const val CONTENT_TYPE = "application/json"
    }
}

fun changeFragments(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.main_fragment_container,
                fragment
            ).commit()
    } else {
        ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_fragment_container,
                fragment
            ).commit()
    }
}

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

fun ImageView.downloadAndSetImage(url: String) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .dontAnimate()
        .placeholder(R.drawable.profile)
        .into(this)
}

fun ImageView.downloadAndSetImageToGroup(url: String) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .dontAnimate()
        .placeholder(R.drawable.group)
        .into(this)
}

inline fun getURL(path: StorageReference, crossinline function: (url: String) -> Unit) =
    path.downloadUrl.addOnSuccessListener { function(it.toString()) }

inline fun putFileInStorage(
    uri: Uri,
    path: StorageReference,
    crossinline function: () -> Unit
) {
    path.putFile(uri).addOnSuccessListener { function() }.addOnFailureListener {
        Log.d(
            "TAGCHECKERROR",
            "putFileInStorage:   ${it.message}"
        )}
}

inline fun putUrlInDatabase(url: String, userId: String, db: DatabaseReference, crossinline function: () -> Unit) {
    db.child("users").child(
        userId
    ).child("photo_url_avatar").setValue(url)
        .addOnSuccessListener { function() }
}

inline fun putUrlGroupInDatabase(url: String, groupId: String, db: DatabaseReference, crossinline function: () -> Unit) {
    db.child("groups").child(
        groupId
    ).child("groupIcon").setValue(url)
        .addOnSuccessListener { function() }
}

fun hasPermissions(context: Context) =
    EasyPermissions.hasPermissions(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )