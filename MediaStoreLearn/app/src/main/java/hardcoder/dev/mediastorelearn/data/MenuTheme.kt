package hardcoder.dev.mediastorelearn.data

import androidx.annotation.DrawableRes

data class MenuTheme(
    val id: Int,
    val title: String,
    @DrawableRes val drawableEndResourceId: Int
)
