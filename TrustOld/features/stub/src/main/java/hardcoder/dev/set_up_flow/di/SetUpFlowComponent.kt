package hardcoder.dev.set_up_flow.di

import dagger.Component
import hardcoder.dev.common_base.di.BaseModule
import hardcoder.dev.common_base.di.ViewModelModule
import hardcoder.dev.set_up_flow.presentation.setUpAppearance.SetUpAppearanceFragment
import hardcoder.dev.set_up_flow.presentation.setUpStub.SetUpStubFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [BaseModule::class, ViewModelModule::class, SetUpFlowModule::class])
interface SetUpFlowComponent {

    fun inject(setUpStubFragment: SetUpStubFragment)
    fun inject(setUpAppearanceFragment: SetUpAppearanceFragment)
}