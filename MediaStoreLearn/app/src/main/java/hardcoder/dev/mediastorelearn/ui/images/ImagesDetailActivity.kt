package hardcoder.dev.mediastorelearn.ui.images

import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import hardcoder.dev.mediastorelearn.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImagesDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_detail)

        retrieveImageMetadata()
    }

    private fun retrieveImageMetadata() = lifecycleScope.launch(Dispatchers.IO) {
        val thumbByteArray = intent.getByteArrayExtra("thumbByteArray")!!
        val thumbnail = BitmapFactory.decodeByteArray(thumbByteArray, 0, thumbByteArray.size)
        val uri = intent.getStringExtra("uri")!!.toUri()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val photoUri = MediaStore.setRequireOriginal(uri)
            contentResolver.openInputStream(photoUri)?.use { inputStream ->
                ExifInterface(inputStream).run {
                    val output = floatArrayOf()
                    val latLong = getLatLong(output) ?: doubleArrayOf(0.0, 0.0)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ImagesDetailActivity, "Lat long is $latLong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}