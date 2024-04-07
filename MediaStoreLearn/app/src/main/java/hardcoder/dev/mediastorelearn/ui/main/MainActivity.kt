package hardcoder.dev.mediastorelearn.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hardcoder.dev.mediastorelearn.R
import hardcoder.dev.mediastorelearn.adapters.MenuThemeAdapter
import hardcoder.dev.mediastorelearn.ui.audio.AudioActivity
import hardcoder.dev.mediastorelearn.ui.grantPermissions.GrantPermissionsActivity
import hardcoder.dev.mediastorelearn.ui.images.ImagesActivity
import hardcoder.dev.mediastorelearn.ui.video.VideoActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = MenuThemeAdapter {
        startActivity(
            Intent(
                this, when (it.id) {
                    0 -> GrantPermissionsActivity::class.java
                    1 -> VideoActivity::class.java
                    2 -> AudioActivity::class.java
                    3 -> ImagesActivity::class.java
                    else -> GrantPermissionsActivity::class.java
                }
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        adapter.submitList(MenuThemeStorage.items)
        menuThemeRecyclerView.adapter = adapter
    }
}