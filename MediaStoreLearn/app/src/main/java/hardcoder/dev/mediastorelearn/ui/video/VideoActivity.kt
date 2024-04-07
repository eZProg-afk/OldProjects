package hardcoder.dev.mediastorelearn.ui.video

import android.content.ContentUris
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Size
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import hardcoder.dev.mediastorelearn.R
import hardcoder.dev.mediastorelearn.adapters.VideoAdapter
import hardcoder.dev.mediastorelearn.data.Video
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoActivity : AppCompatActivity() {

    private val videoAdapter by lazy {
        VideoAdapter { video: Video ->

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        setUpClicks()
        setUpRecyclerView()
        fetchVideos()
    }

    private fun setUpClicks() {
        goBackTextView.setOnClickListener {
            finish()
        }
    }

    private fun setUpRecyclerView() {
        dataRecyclerView.adapter = videoAdapter
    }

    private fun fetchVideos() = lifecycleScope.launch(Dispatchers.IO) {
        val videoList = mutableListOf<Video>()
        val contentResolver = applicationContext.contentResolver

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE
        )


        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

        contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idCursor = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameCursor = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationCursor = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCursor)
                val name = cursor.getString(nameCursor)
                val duration = cursor.getInt(durationCursor)
                val size = cursor.getInt(sizeColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val thumbnail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentResolver.loadThumbnail(contentUri, Size(600, 600), null)
                } else {
                    ThumbnailUtils.createVideoThumbnail(
                        requireNotNull(contentUri.path),
                        MediaStore.Images.Thumbnails.MINI_KIND
                    )
                }

                val video = Video(contentUri, name, duration, size, requireNotNull(thumbnail))
                videoList += video
            }
        }

        withContext(Dispatchers.Main) {
            Toast.makeText(this@VideoActivity, "Total count - ${videoList.size}", Toast.LENGTH_SHORT).show()
            videoAdapter.submitList(videoList)
        }
    }

    private fun openAndPlayVideo(video: Video) {

    }
}