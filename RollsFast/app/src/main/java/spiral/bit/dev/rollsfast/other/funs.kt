package spiral.bit.dev.rollsfast.other

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import pub.devrel.easypermissions.EasyPermissions
import spiral.bit.dev.rollsfast.R
import spiral.bit.dev.rollsfast.model.User

//TODO, ЧТОБЫ НЕ ТЕРЯЛСЯ
//ЗА ЗАВТРА МНЕ НУЖНО ЗАПИЛИТЬ АДМИНКУ С ДОБАВЛЕНИЕМ ФОТКИ ВКЛЮЧИТЕЛЬНО,ТАКЖЕ СПИННЕР, --- готово
//И НАКИНУТЬ ШИММЕР НА РЕСАЙКЛЕР
//позже; добавить другие способы регистрации / авторизации
//Также создать детализированную активити о продукте,и реализовать его покупку (гугл пей)
// и также сделать активити как у тануки (ваш заказ готовится,а также слать пуши по изменению статуса заказа)
//+ можно добавить сплэш скрин, найти аву на прогу, и, думаю, всё.

lateinit var REFERENCE_DATABASE: DatabaseReference
lateinit var REFERENCE_STORAGE: StorageReference
const val NODE_USERS = "users"
const val NODE_PRODUCTS = "products"
lateinit var USER: User
lateinit var CURRENT_USER_ID: String

fun hasPermissions(context: Context) =
    EasyPermissions.hasPermissions(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )

inline fun putUrlInDatabase(url: String, productID: String, type: String, crossinline function: () -> Unit) {
    REFERENCE_DATABASE.child(NODE_PRODUCTS).child(type).child(
        productID
    ).child("product_image").setValue(url)
        .addOnSuccessListener { function() }
}

inline fun initUser(crossinline function: () -> Unit) {
    REFERENCE_DATABASE.child(NODE_USERS).child(
        CURRENT_USER_ID
    ).addListenerForSingleValueEvent(AppValueEventListener {
        USER =
            it.getValue(User::class.java)
                ?: User()
        if (USER.userName.isEmpty()) USER.userName = CURRENT_USER_ID
        function()
    })
}

inline fun getURL(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
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
        .placeholder(R.drawable.picture)
        .into(this)
}