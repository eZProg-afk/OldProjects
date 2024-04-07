package spiral.bit.dev.rollsfast.other

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import spiral.bit.dev.rollsfast.R

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(getString(R.string.yandex_api_key))
    }
}