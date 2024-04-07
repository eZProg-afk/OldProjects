package hardcoder.dev.common_base.providers

import android.app.Application
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import javax.inject.Inject

class ResourceProvider @Inject constructor(private val application: Application) {

    fun getDrawable(@DrawableRes id: Int) = ResourcesCompat.getDrawable(application.resources, id, application.theme)

    fun getDimen(@DimenRes id: Int) = application.resources.getDimension(id)

    fun getString(@StringRes id: Int) = application.resources.getString(id)

    fun getAnimation(@AnimRes id: Int) = application.resources.getAnimation(id)

    fun getColor(@ColorRes id: Int) = application.getColor(id)

}