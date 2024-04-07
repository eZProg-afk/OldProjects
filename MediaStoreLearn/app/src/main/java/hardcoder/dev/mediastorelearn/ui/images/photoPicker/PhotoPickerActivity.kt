package hardcoder.dev.mediastorelearn.ui.images.photoPicker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import hardcoder.dev.mediastorelearn.R
import kotlinx.android.synthetic.main.activity_photo_picker.*

class PhotoPickerActivity : AppCompatActivity() {

    private var mediaCountToPick = 1

    val pickSinglePhotoOnly = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let {

        }
    }

    val pickMultiplePhotosOnly = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(mediaCountToPick)) { uris ->
        uris.forEach {

        }
    }

    val pickSingleVideoOnly = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let {

        }
    }

    val pickMultipleVideosOnly = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(mediaCountToPick)) { uris ->
        uris.forEach {

        }
    }

    val pickSinglePhotoOrVideo = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let {

        }
    }

    val pickMultiplePhotoAndVideos = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(mediaCountToPick)) { uris ->
        uris.forEach {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_picker)

        setUpClicks()
        setUpViews()
    }

    private fun setUpViews() {
        chooseMediaCountNumberPicker.apply {
            minValue = 2
            maxValue = 100
            setOnValueChangedListener { _, _, newValue ->
                mediaCountToPick = newValue
            }
        }
    }

    private fun setUpClicks() {
        goBackTextView.setOnClickListener {
            finish()
        }

        selectionModeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            chooseMediaCountNumberPicker.isVisible = !isChecked
        }

        viewAttachedResultsTextView.setOnClickListener {
            startActivity(Intent(this, ViewAttachedResultsActivity::class.java))
        }

        pickOnlyPhotoTextView.setOnClickListener {
            if (mediaCountToPick == 1) {
                pickSinglePhotoOnly.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                pickMultiplePhotosOnly.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        pickOnlyVideoTextView.setOnClickListener {
            if (mediaCountToPick == 1) {
                pickSingleVideoOnly.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
            } else {
                pickMultipleVideosOnly.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
            }
        }

        pickPhotoAndVideoTextView.setOnClickListener {
            if (mediaCountToPick == 1) {
                pickSinglePhotoOrVideo.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            } else {
                pickMultiplePhotoAndVideos.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            }
        }
    }
}