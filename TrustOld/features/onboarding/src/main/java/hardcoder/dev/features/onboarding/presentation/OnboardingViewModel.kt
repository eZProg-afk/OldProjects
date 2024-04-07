package hardcoder.dev.features.onboarding.presentation

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDeepLinkRequest
import hardcoder.dev.common_base.providers.DeepLinkProvider
import hardcoder.dev.common_base.providers.DeepLinks
import hardcoder.dev.common_base.providers.ResourceProvider
import hardcoder.dev.features.onboarding.OnboardingStorage
import hardcoder.dev.features.onboarding.R
import hardcoder.dev.features.onboarding.presentation.adapters.ItemOnboarding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class OnboardingViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val deepLinkProvider: DeepLinkProvider
) : ViewModel() {

    private var _onboardingItems = MutableStateFlow(OnboardingStorage.onboardingItems)
    val onboardingItems: StateFlow<List<ItemOnboarding>> get() = _onboardingItems

    private var _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun toNextPage() {
        val currentPosition = uiState.value.currentItemPosition
        if (currentPosition < END_ITEM_POSITION) {
            _uiState.update { state ->
                state.copy(currentItemPosition = currentPosition + 1)
            }
        } else {
            _uiState.update { state ->
                state.copy(isFinishedReading = true)
            }
        }
    }

    fun pageManually(position: Int) {
        _uiState.update { state ->
            state.copy(currentItemPosition = position)
        }
    }

    fun skipOnboarding() {
        _uiState.update { state ->
            state.copy(
                currentItemPosition = END_ITEM_POSITION,
                isFinishedReading = true
            )
        }
    }

    fun getIndicatorIcon(isActive: Boolean): Drawable? {
        return if (isActive) resourceProvider.getDrawable(R.drawable.indicator_active_background)
        else resourceProvider.getDrawable(R.drawable.indicator_inactive_background)
    }

    fun buildDeepLinkToSetUpFlow(): NavDeepLinkRequest {
        val uri = deepLinkProvider.buildUri(destinationName = DeepLinks.setUpStub)
        return deepLinkProvider.buildRequest(uri)
    }

    private companion object {
        private const val START_ITEM_POSITION = 0
        private const val END_ITEM_POSITION = 2
        private val initialUiState = OnboardingUiState(
            currentItemPosition = START_ITEM_POSITION,
            isFinishedReading = false
        )
    }
}