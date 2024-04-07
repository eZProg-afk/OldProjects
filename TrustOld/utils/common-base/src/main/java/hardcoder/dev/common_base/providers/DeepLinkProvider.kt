package hardcoder.dev.common_base.providers

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import javax.inject.Inject

class DeepLinkProvider @Inject constructor(private val application: Application) {

    fun buildUri(mainModule: String = "android-app", destinationName: String): Uri {
        return "$mainModule://${application.packageName}/$destinationName".toUri()
    }

    fun buildRequest(uri: Uri): NavDeepLinkRequest {
        return NavDeepLinkRequest.Builder
            .fromUri(uri)
            .build()
    }
}