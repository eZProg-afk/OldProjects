package hardcoder.dev.mediastorelearn.ui.main

import hardcoder.dev.mediastorelearn.R
import hardcoder.dev.mediastorelearn.data.MenuTheme

object MenuThemeStorage {

    val items = listOf(
        MenuTheme(
            id = 0,
            title = "Grant permissions",
            drawableEndResourceId = R.drawable.ic_sdcard
        ),
        MenuTheme(
            id = 1,
            title = "Fetch videos",
            drawableEndResourceId = R.drawable.ic_video
        ),
        MenuTheme(
            id = 2,
            title = "Fetch audiofiles",
            drawableEndResourceId = R.drawable.ic_audio
        ),
        MenuTheme(
            id = 3,
            title = "Fetch images",
            drawableEndResourceId = R.drawable.ic_image
        ),
    )
}