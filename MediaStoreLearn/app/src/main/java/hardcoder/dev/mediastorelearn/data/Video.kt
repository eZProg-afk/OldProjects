package hardcoder.dev.mediastorelearn.data

import android.graphics.Bitmap
import android.net.Uri

data class Video(
    val uri: Uri,
    val name: String,
    val duration: Int,
    val size: Int,
    val thumbnail: Bitmap
)
