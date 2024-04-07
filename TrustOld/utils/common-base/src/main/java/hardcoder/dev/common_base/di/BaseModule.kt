package hardcoder.dev.common_base.di

import android.app.Application
import dagger.Module
import dagger.Provides
import hardcoder.dev.common_base.providers.DeepLinkProvider
import hardcoder.dev.common_base.providers.ResourceProvider
import javax.inject.Singleton

@Module
class BaseModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideResourcesProvider() = ResourceProvider(application)

    @Singleton
    @Provides
    fun provideDeepLinkProvider() = DeepLinkProvider(application)
}