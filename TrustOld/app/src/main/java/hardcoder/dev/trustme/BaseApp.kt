package hardcoder.dev.trustme

import android.app.Application
import com.google.android.material.color.DynamicColors

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}