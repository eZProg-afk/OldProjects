package hardcoder.dev.common_base.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds //provides the "generic" ViewModel factory created
    abstract fun bindsViewModelFactory(factory: MySingletonViewModelFactory): ViewModelProvider.Factory
}