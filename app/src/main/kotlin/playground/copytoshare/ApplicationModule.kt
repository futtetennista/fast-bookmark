package playground.copytoshare

import android.app.Application
import android.content.Context
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

  @Provides @Singleton fun preferences(): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(application)
  }

  @Provides @Singleton fun context(): Context {
    return application.applicationContext
  }
}
