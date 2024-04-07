package hardcoder.dev.set_up_flow.presentation.setUpStub

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import hardcoder.dev.common_base.base.BaseFragment
import hardcoder.dev.common_base.di.BaseModule
import hardcoder.dev.common_base.extensions.launchWith
import hardcoder.dev.common_base.providers.DeepLinkProvider
import hardcoder.dev.set_up_flow.R
import hardcoder.dev.set_up_flow.databinding.FragmentSetUpStubBinding
import hardcoder.dev.set_up_flow.di.DaggerSetUpFlowComponent
import hardcoder.dev.set_up_flow.presentation.adapters.stubDelegate
import hardcoder.dev.set_up_flow.presentation.adapters.stubDiffCallback
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SetUpStubFragment :
    BaseFragment<FragmentSetUpStubBinding>(FragmentSetUpStubBinding::inflate) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<SetUpStubViewModel> { factory }
    private val adapter = AsyncListDifferDelegationAdapter(stubDiffCallback, stubDelegate { item ->
        viewModel.selectStub(item.id)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDI()
        setUpRecyclerView()
        subscribeToObservables()
    }

    private fun setUpDI() {
        DaggerSetUpFlowComponent
            .builder()
            .baseModule(BaseModule(requireActivity().application))
            .build()
            .inject(this)
    }

    private fun setUpRecyclerView() = binding {
        stubRecyclerView.adapter = adapter
    }

    private fun subscribeToObservables() = binding {
        viewModel.uiState.onEach { state ->
            if (state.isSelected) {
                findNavController().navigate(R.id.toSetUpAppearanceFragment)
            } else {

            }
        }.launchWith(viewLifecycleOwner)

        viewModel.stubs.onEach { stubs ->
            adapter.items = stubs
        }
    }
}