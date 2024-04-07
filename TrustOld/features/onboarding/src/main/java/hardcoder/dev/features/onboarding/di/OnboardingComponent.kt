package hardcoder.dev.features.onboarding.di

import dagger.Component
import hardcoder.dev.common_base.di.BaseModule
import hardcoder.dev.common_base.di.ViewModelModule
import hardcoder.dev.features.onboarding.presentation.OnboardingFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [BaseModule::class, ViewModelModule::class, OnboardingModule::class])
interface OnboardingComponent {

    fun inject(onboardingFragment: OnboardingFragment)
}