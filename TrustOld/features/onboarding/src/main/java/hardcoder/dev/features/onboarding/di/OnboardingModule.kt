package hardcoder.dev.features.onboarding.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import hardcoder.dev.common_base.di.ViewModelKey
import hardcoder.dev.features.onboarding.presentation.OnboardingViewModel

@Module
abstract class OnboardingModule {

    @Binds
    @IntoMap
    @ViewModelKey(OnboardingViewModel::class)
    abstract fun bindsOnboardingViewModel(viewModel: OnboardingViewModel): ViewModel
}