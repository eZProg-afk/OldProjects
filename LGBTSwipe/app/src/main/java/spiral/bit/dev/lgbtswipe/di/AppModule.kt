package spiral.bit.dev.lgbtswipe.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import spiral.bit.dev.lgbtswipe.other.MAIN_PREFS_NAME
import spiral.bit.dev.lgbtswipe.other.NetworkConnection
import spiral.bit.dev.lgbtswipe.other.PREFS_MODE
import java.text.DateFormat
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context)
    : SharedPreferences = app.getSharedPreferences(MAIN_PREFS_NAME, PREFS_MODE)

    @Singleton
    @Provides
    fun provideAuthClient() = FirebaseAuth.getInstance()

    @Provides
    fun provideCalendarInstance(): Calendar = Calendar.getInstance()

    @Singleton
    @Provides
    fun provideSimpleDateFormatInstance(): DateFormat = DateFormat.getDateInstance(DateFormat.FULL)

    @Singleton
    @Provides
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideNetworkManager(@ApplicationContext app: Context) = NetworkConnection(app)
}