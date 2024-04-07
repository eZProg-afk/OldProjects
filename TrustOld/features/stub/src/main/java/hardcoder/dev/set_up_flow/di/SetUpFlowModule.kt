package hardcoder.dev.set_up_flow.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hardcoder.dev.common_base.di.ViewModelKey
import hardcoder.dev.set_up_flow.presentation.setUpStub.SetUpStubViewModel
import javax.inject.Singleton

@Module
abstract class SetUpFlowModule {

    @Binds
    @IntoMap
    @ViewModelKey(SetUpStubViewModel::class)
    abstract fun bindsSetUpStubViewModel(viewModel: SetUpStubViewModel): ViewModel
}