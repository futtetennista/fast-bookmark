package com.stefano.playground.fastbookmark.background.service

import android.content.Context
import android.content.pm.PackageManager
import com.stefano.playground.fastbookmark.inject.ServiceScope
import com.stefano.playground.fastbookmark.utils.AppPreferences
import dagger.Module
import dagger.Provides

@Module
class ServiceModule {

  @Provides @ServiceScope fun provideFastBookmarker(context: Context,
                                      packageManager: PackageManager,
                                      preferences: AppPreferences): FastBookmarker {
    return FastBookmarker(context, packageManager, preferences)
  }
}