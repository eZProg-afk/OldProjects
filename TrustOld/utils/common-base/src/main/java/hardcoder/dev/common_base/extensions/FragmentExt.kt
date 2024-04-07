package hardcoder.dev.common_base.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.navigateToDeepLink(request: NavDeepLinkRequest) {
    findNavController().navigate(request)
}