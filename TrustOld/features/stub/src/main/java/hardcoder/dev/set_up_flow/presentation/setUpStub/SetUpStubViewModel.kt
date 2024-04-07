package hardcoder.dev.set_up_flow.presentation.setUpStub

import androidx.lifecycle.ViewModel
import hardcoder.dev.common_base.providers.ResourceProvider
import hardcoder.dev.set_up_flow.presentation.adapters.ItemStub
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SetUpStubViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private var _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<SetUpStubUiState> = _uiState.asStateFlow()

    private var _stubs = MutableStateFlow<List<ItemStub>>(emptyList())
    val stubs: StateFlow<List<ItemStub>> = _stubs.asStateFlow()

    fun selectStub(stubId: Int) {
        _uiState.update { state ->
            state.copy(isSelected = true, selectedStubIndex = stubId)
        }
        // TODO SAVE SELECTED STUB TO DATA STORE AND NAVIGATE TO NEXT SETUP
    }

    fun previewStub(selectedStubIndex: Int) {
        _uiState.update { state ->
            state.copy(
                selectedStubIndex = selectedStubIndex,
                previewVideoResourceId = when (selectedStubIndex) {

                    else -> {
                        0
                    } // TODO PUT CALCULATOR STUB PREVIEW HERE
                }
            )
        }
    }

    private companion object {
        private val initialState = SetUpStubUiState(
            selectedStubIndex = 0,
            previewVideoResourceId = 0, // TODO PUT CALCULATOR STUB PREVIEW HERE
            isSelected = false
        )
    }
}