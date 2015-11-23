package playground.copytoshare

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import playground.copytoshare.utils.AppPreferences
import javax.inject.Singleton

@Module
class ApplicationModule {

  private var application: Application

  constructor(application: Application) {
    this.application = application
  }

  @Provides @Singleton fun preferences(): AppPreferences {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    return AppPreferences(sharedPreferences)
  }

  @Provides @Singleton fun context(): Context {
    return application.applicationContext
  }
}
