package hardcoder.dev.mediastorelearn.ui.video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hardcoder.dev.mediastorelearn.R
import kotlinx.android.synthetic.main.activity_video.*

class VideoDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        setUpClicks()
    }

    private fun setUpClicks() {
        goBackTextView.setOnClickListener {
            finish()
        }
    }
}