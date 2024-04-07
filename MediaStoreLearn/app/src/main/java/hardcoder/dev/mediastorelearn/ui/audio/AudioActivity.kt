package hardcoder.dev.mediastorelearn.ui.audio

import android.content.ContentUris
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import hardcoder.dev.mediastorelearn.R
import hardcoder.dev.mediastorelearn.adapters.AudioAdapter
import hardcoder.dev.mediastorelearn.data.Audio
import kotlinx.android.synthetic.main.activity_audio.*
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.activity_video.dataRecyclerView
import kotlinx.android.synthetic.main.activity_video.goBackTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioActivity : AppCompatActivity() {

    private val audioAdapter by lazy {
        AudioAdapter { audio: Audio ->

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        setUpRecyclerView()
        fetchAudios()
        setUpClicks()
    }

    private fun setUpRecyclerView() {
        dataRecyclerView.adapter = audioAdapter
    }

    private fun fetchAudios() = lifecycleScope.launch(Dispatchers.IO) {
        val contentResolver = applicationContext.contentResolver
        val audioList = mutableListOf<Audio>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection =
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    MediaStore.Audio.Media.AUTHOR
                } else {
                    MediaStore.Audio.Media.COMPOSER
                }
            )
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)

            val authorOrComposerColumn = cursor.getColumnIndexOrThrow(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    MediaStore.Audio.Media.AUTHOR
                } else {
                    MediaStore.Audio.Media.COMPOSER
                }
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                val album = cursor.getString(albumColumn)
                val authorOrComposer = cursor.getString(authorOrComposerColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val audio = Audio(contentUri, name, duration, size, album, authorOrComposer)
                audioList += audio
            }
        }

        withContext(Dispatchers.Main) {
            Toast.makeText(this@AudioActivity, "Total count - ${audioList.size}", Toast.LENGTH_SHORT).show()
            audioAdapter.submitList(audioList)
        }
    }

    private fun setUpClicks() {
        goBackTextView.setOnClickListener {
            finish()
        }
    }
}