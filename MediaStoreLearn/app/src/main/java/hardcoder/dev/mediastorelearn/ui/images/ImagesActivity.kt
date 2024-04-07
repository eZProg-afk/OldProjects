package hardcoder.dev.mediastorelearn.ui.images

import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Size
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import hardcoder.dev.mediastorelearn.R
import hardcoder.dev.mediastorelearn.adapters.ImageAdapter
import hardcoder.dev.mediastorelearn.data.Image
import hardcoder.dev.mediastorelearn.ui.images.photoPicker.PhotoPickerActivity
import kotlinx.android.synthetic.main.activity_images.*
import kotlinx.android.synthetic.main.activity_video.goBackTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class ImagesActivity : AppCompatActivity() {

    private val adapter by lazy {
        ImageAdapter { image ->
            val bs = ByteArrayOutputStream()
            image.thumbnail.compress(Bitmap.CompressFormat.PNG, 50, bs)
            Intent(this, ImagesDetailActivity::class.java).apply {
                putExtra("name", image.name)
                putExtra("size", image.size)
                putExtra("uri", image.uri.toString())
                putExtra("thumbByteArray", bs.toByteArray())
            }.also { startActivity(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        setUpClicks()
        setUpRecyclerView()
        fetchImages()
    }

    private fun fetchImages() = lifecycleScope.launch(Dispatchers.IO) {
        val contentResolver = applicationContext.contentResolver
        val imagesList = mutableListOf<Image>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection =
            arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE)
        val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"

        contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val thumbnail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    contentResolver.loadThumbnail(contentUri, Size(600, 600), null)
                } else {
                    ThumbnailUtils.createVideoThumbnail(
                        requireNotNull(contentUri.path),
                        MediaStore.Images.Thumbnails.MINI_KIND
                    )
                }

                val image = Image(contentUri, name, size, requireNotNull(thumbnail))
                imagesList += image
            }
        }

        withContext(Dispatchers.Main) {
            Toast.makeText(this@ImagesActivity, "Total count - ${imagesList.size}", Toast.LENGTH_SHORT).show()
            adapter.submitList(imagesList)
        }
    }

    private fun setUpRecyclerView() {
        dataRecyclerView.adapter = adapter
    }

    private fun setUpClicks() {
        goBackTextView.setOnClickListener {
            finish()
        }

        photoPickerTextView.setOnClickListener {
            startActivity(Intent(this, PhotoPickerActivity::class.java))
        }
    }
}