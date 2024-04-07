package hardcoder.dev.common_base.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn

fun <T> Flow<T>.launchWith(
    lifecycleOwner: LifecycleOwner,
    activeState: Lifecycle.State = Lifecycle.State.RESUMED
) = flowWithLifecycle(lifecycleOwner.lifecycle, activeState)
    .launchIn(lifecycleOwner.lifecycleScope)