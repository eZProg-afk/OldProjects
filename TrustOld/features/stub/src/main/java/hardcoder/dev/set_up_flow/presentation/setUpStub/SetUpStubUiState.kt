package hardcoder.dev.set_up_flow.presentation.setUpStub

import androidx.annotation.RawRes

data class SetUpStubUiState(
    val selectedStubIndex: Int,
    @RawRes val previewVideoResourceId: Int,
    val isSelected: Boolean
)
