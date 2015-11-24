package com.stefano.playground.fastbookmark

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import com.stefano.playground.fastbookmark.utils.AppPreferences
import dagger.Module
import dagger.Provides
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

  @Provides @Singleton fun packageManager(): PackageManager {
    return application.packageManager
  }
}
