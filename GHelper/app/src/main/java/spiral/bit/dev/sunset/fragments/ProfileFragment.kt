package spiral.bit.dev.sunset.fragments

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_profile.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.RegAuthActivity
import spiral.bit.dev.sunset.adapters.ChooseAvatarsFragment
import spiral.bit.dev.sunset.other.*

class ProfileFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference

        profile_user_avatar.setOnClickListener { showSelectAvaDialog(view) }
        profile_email_info.text = USER.userEmail
        profile_id_info.text = USER.userId
        profile_nick_name_et.setText(USER.userNickName)
        profile_btn_log_out.setOnClickListener {
            auth.signOut()
            startActivity(Intent(view.context, RegAuthActivity::class.java))
        }
        profile_btn_apply_changes.setOnClickListener {
            if (profile_nick_name_et.text.isNotEmpty()) {
                USER.userNickName = profile_nick_name_et.text.toString()
                dbRef.child("users").child(USER.userId.toString()).child("userNickName")
                    .setValue(USER.userNickName)
                Toast.makeText(context, "Изменения применены!", Toast.LENGTH_LONG).show()
            } else Toast.makeText(context, "Пожалуйста, введите никнейм!", Toast.LENGTH_LONG).show()
        }
        val path = auth.currentUser?.uid?.let { storage.reference.child("AVATARS_USERS").child(it) }
        if (path != null) getURL(path) { profile_user_avatar.downloadAndSetImage(it) }
    }

    private fun showSelectAvaDialog(viewMain: View) {
        val builder = AlertDialog.Builder(viewMain.context)
        val view: View = LayoutInflater.from(viewMain.context)
            .inflate(
                R.layout.layout_select_ava_dialog,
                viewMain.findViewById(R.id.layout_select_ava_container)
            )
        builder.setView(view)
        val dialogSelectAvatar = builder.create()
        if (dialogSelectAvatar.window != null) dialogSelectAvatar.window?.setBackgroundDrawable(
            ColorDrawable(0)
        )
        view.findViewById<View>(R.id.text_delete_note).setOnClickListener {
            changeFragments(ChooseAvatarsFragment(), true)
            dialogSelectAvatar.dismiss()
        }
        view.findViewById<View>(R.id.text_cancel).setOnClickListener {
            ImagePicker.create(this)
                .returnMode(ReturnMode.NONE)
                .folderMode(true)
                .toolbarFolderTitle("Добавление аватарки")
                .toolbarImageTitle("Выбрать картинку")
                .toolbarArrowColor(Color.WHITE)
                .single()
                .showCamera(true)
                .imageDirectory("Camera")
                .start()
            dialogSelectAvatar.dismiss()
        }
        dialogSelectAvatar.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val receiveImage: Image? = ImagePicker.getFirstImageOrNull(data)
            val path = storage.reference.child("AVATARS_USERS")
                .child(auth.currentUser?.uid.toString())
            putFileInStorage(Uri.parse(receiveImage?.uri.toString()), path) {
                getURL(path) {
                    putUrlInDatabase(it, auth.currentUser?.uid.toString(), dbRef) {
                        Toast.makeText(context, "Фото добавлено!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun requestPermissions() {
        if (hasPermissions(requireContext())) return
        EasyPermissions.requestPermissions(
            this,
            "Вам нужно принять разрешения, чтобы корректно работать с приложением!)",
            222,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}