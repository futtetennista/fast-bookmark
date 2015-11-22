package copytoshare.inject

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

  private var application: Application

  constructor(application: Application) {
    this.application = application
  }

  @Provides @Singleton fun providePreferences(): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(application)
  }
}