package hardcoder.dev.mediastorelearn.ui.grantPermissions

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import hardcoder.dev.mediastorelearn.R
import kotlinx.android.synthetic.main.activity_grant_permissions.*

class GrantPermissionsActivity : AppCompatActivity() {

    private val storagePermissionsContract =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { entry ->
                val isGranted = entry.value
                if (isGranted) {
                    Toast.makeText(this, "All ok, permissions is granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "This is required permission", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grant_permissions)
        setUpClicks()
    }

    private fun setUpClicks() {
        goBackTextView.setOnClickListener {
            finish()
        }

        checkPermissionsTextView.setOnClickListener {
            queryPermissions()
        }
    }

    private fun queryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissionsContract.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.ACCESS_MEDIA_LOCATION
                )
            )
        } else {
            storagePermissionsContract.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }
}