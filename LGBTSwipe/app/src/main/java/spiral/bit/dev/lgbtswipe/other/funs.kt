package spiral.bit.dev.lgbtswipe.other

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.StorageReference
import pub.devrel.easypermissions.EasyPermissions
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.models.Model
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.views.main.BaseActivity
import spiral.bit.dev.lgbtswipe.views.reg_auth.RegActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


lateinit var ACTIVITY: RegActivity
lateinit var MAIN_ACTIVITY: BaseActivity
private val deleteOptions = arrayOf("Удалить у всех пользователей", "Только у меня", "Отмена")

fun changeFragments(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
            .replace(
                R.id.reg_auth_fragment,
                fragment
            )
            .commit()
    } else {
        ACTIVITY.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
            .replace(
                R.id.reg_auth_fragment,
                fragment
            )
            .commit()
    }
}

fun changeMainFragments(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        MAIN_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
            .replace(
                R.id.main_fragment,
                fragment
            )
            .commit()
    } else {
        MAIN_ACTIVITY.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
            .replace(
                R.id.main_fragment,
                fragment
            )
            .commit()
    }
}

fun DataSnapshot.getModel(): Model =
    this.getValue(Model::class.java) ?: Model()

fun DataSnapshot.getUser(): User? =
    this.getValue(User::class.java) ?: AUTH.currentUser?.uid?.let { User(it) }

fun getFileFromStorage(mFile: File, fileUrl: String, function: () -> Unit) {
    val path = REFERENCE_STORAGE.storage.getReferenceFromUrl(fileUrl)
    path.getFile(mFile)
        .addOnSuccessListener { function() }
        .addOnFailureListener { //showToast(it.message.toString())
        }
}

inline fun putUrlInDatabase(url: String, crossinline function: () -> Unit) {
    REFERENCE_DATABASE.child(NODE_USERS).child(
        CURRENT_USER_ID
    ).child(CHILD_PHOTO_URL_AVATAR).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { //showToast(it.message.toString())
        }
}

inline fun initUser(crossinline function: () -> Unit) {
    REFERENCE_DATABASE.child(NODE_USERS).child(
        CURRENT_USER_ID
    ).addListenerForSingleValueEvent(AppValueEventListener {
        USER =
            it.getValue(User::class.java)
                ?: User()
        if (USER.name.isEmpty()) {
            USER.name =
                CURRENT_USER_ID
        }
        function()
    })
}

inline fun getURL(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener {
            //showToast(it.message.toString())
        }
}

inline fun putFileInStorage(
    uri: Uri,
    path: StorageReference,
    crossinline function: () -> Unit
) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
}

fun ImageView.downloadAndSetImage(url: String) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .dontAnimate()
        .placeholder(R.drawable.profile)
        .into(this)
}


fun hasPermissions(context: Context) =
    EasyPermissions.hasPermissions(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun getMsgKey(id: String) = REFERENCE_DATABASE.child(
    NODE_MESSAGES
).child(CURRENT_USER_ID)
    .child(id).push().key.toString()

fun uploadFileInStorage(
    uri: Uri,
    messageKey: String,
    receivedID: String,
    typeMessage: String,
    filename: String = ""
) {
    val path = REFERENCE_STORAGE.child(
        FILES_USERS
    ).child(messageKey)
    putFileInStorage(uri, path) {
        sendMsgAsFile(
            receivedID,
            uri.toString(),
            messageKey,
            typeMessage,
            filename
        )
    }
}

fun hideKeyboard() {
    val imm: InputMethodManager = ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    imm.hideSoftInputFromWindow(ACTIVITY.window.decorView.windowToken, 0)
}

fun sendMsgAsFile(
    receivingUserID: String,
    fileUrl: String,
    messageKey: String,
    typeMessage: String,
    filename: String
) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_USER_ID/$receivingUserID"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/$CURRENT_USER_ID"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM_MOON] = CURRENT_USER_ID
    mapMessage[CHILD_TYPE_MOON] = typeMessage
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIME_STAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_FILE_URL_MOON] = fileUrl
    mapMessage[CHILD_TEXT_MOON] = filename

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REFERENCE_DATABASE
        .updateChildren(mapDialog)
        .addOnFailureListener { //showToast(it.message.toString())
        }
}

fun saveToChatList(id: String, type: String) {
    val refUser = "$NODE_CHAT_LIST/$CURRENT_USER_ID/$id"
    val refReceived = "$NODE_CHAT_LIST/$id/$CURRENT_USER_ID"

    val mapUser = hashMapOf<String, Any>()
    val mapReceived = hashMapOf<String, Any>()

    mapUser[CHILD_ID] = id
    mapUser[CHILD_TYPE_MOON] = type

    mapReceived[CHILD_ID] = CURRENT_USER_ID
    mapReceived[CHILD_TYPE_MOON] = type

    val commonMap = hashMapOf<String, Any>()
    commonMap[refUser] = mapUser
    commonMap[refReceived] = mapReceived

    REFERENCE_DATABASE.updateChildren(commonMap)
        .addOnFailureListener {
            //showToast(it.message.toString())
         }

}


fun sendMsg(message: String, receivingUserID: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_USER_ID/$receivingUserID"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/$CURRENT_USER_ID"
    val messageKey = REFERENCE_DATABASE.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM_MOON] =
        CURRENT_USER_ID
    mapMessage[CHILD_TYPE_MOON] = typeText
    mapMessage[CHILD_TEXT_MOON] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIME_STAMP_MOON] =
        ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REFERENCE_DATABASE
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener {
            //showToast(it.message.toString())
        }

}

fun getFilenameFromUri(uri: Uri): String {
    var result = ""
    val cursor = ACTIVITY.contentResolver.query(uri, null, null, null, null)
    try {
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    } catch (e: Exception) {
        //showToast(e.message.toString())
    } finally {
        cursor?.close()
        return result
    }
}

fun uploadVideoInStorage(
    uri: Uri,
    messageKey: String,
    receivedID: String,
    typeMessage: String,
    filename: String = ""
) {
    val path = REFERENCE_STORAGE.child(
        FILES_USERS
    ).child(messageKey)
    putFileInStorage(uri, path) {
        sendMsgAsFile(
            receivedID,
            uri.toString(),
            messageKey,
            typeMessage,
            filename
        )
    }
}

fun clearChat(idMoon: String, function: () -> Unit) {
    val dialog = AlertDialog.Builder(ACTIVITY)
    dialog.setIcon(R.drawable.ic_delete)
        .setCancelable(true)
        .setItems(deleteOptions) { currentDialog, position ->
            when (position) {
                0 -> {
                    REFERENCE_DATABASE.child(NODE_MESSAGES).child(CURRENT_USER_ID)
                        .child(idMoon).removeValue()
                        .addOnSuccessListener {
                            REFERENCE_DATABASE.child(NODE_MESSAGES)
                                .child(idMoon).child(CURRENT_USER_ID).removeValue()
                                .addOnSuccessListener {
                                    function()
                                }
                        }
                }
                1 -> {
                    REFERENCE_DATABASE.child(NODE_MESSAGES).child(CURRENT_USER_ID)
                        .child(idMoon).removeValue()
                        .addOnSuccessListener {
                            function()
                        }
                }
                else -> currentDialog.cancel()
            }
        }
    dialog.show()
}

fun deleteChat(idMoon: String, function: () -> Unit) {
    REFERENCE_DATABASE.child(NODE_CHAT_LIST).child(CURRENT_USER_ID)
        .child(idMoon).removeValue()
        .addOnFailureListener {
            //showToast(it.message.toString())
            }
        .addOnSuccessListener { function() }
}

