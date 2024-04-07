package hardcoder.dev.mediastorelearn.data

import android.net.Uri

data class Audio(
    val uri: Uri,
    val name: String,
    val duration: Int,
    val size: Int,
    val album: String,
    val authorOrComposer: String?
)
