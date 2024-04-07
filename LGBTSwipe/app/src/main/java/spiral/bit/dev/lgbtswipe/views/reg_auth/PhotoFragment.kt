package spiral.bit.dev.lgbtswipe.views.reg_auth

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentPhotoBinding
import spiral.bit.dev.lgbtswipe.other.changeFragments
import spiral.bit.dev.lgbtswipe.other.hasPermissions
import javax.inject.Inject

@AndroidEntryPoint
class PhotoFragment : Fragment(R.layout.fragment_photo), EasyPermissions.PermissionCallbacks {

    @Inject
    lateinit var mainPrefs: SharedPreferences

    @Inject
    lateinit var storageInstance: FirebaseStorage

    @Inject
    lateinit var authClient: FirebaseAuth

    private val photoBinding: FragmentPhotoBinding by viewBinding(FragmentPhotoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        photoBinding.addImageIv.setOnClickListener {
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
        }
        photoBinding.btnNext.setOnClickListener {
            changeFragments(fragment = PasswordFragment(), addStack = true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val receiveImage: Image? = ImagePicker.getFirstImageOrNull(data)
            mainPrefs.edit().putString("avatar", receiveImage?.path).apply()
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