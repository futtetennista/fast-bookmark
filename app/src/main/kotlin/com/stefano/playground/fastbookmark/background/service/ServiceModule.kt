package com.stefano.playground.fastbookmark.background.service

import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import com.stefano.playground.fastbookmark.inject.ServiceScope
import com.stefano.playground.fastbookmark.utils.AppPreferences
import com.stefano.playground.fastbookmark.utils.PackageManagerDelegateImpl
import dagger.Module
import dagger.Provides

@Module
class ServiceModule {

  @Provides @ServiceScope fun provideIntentFactory(context: Context): IntentFactory {
    return IntentFactoryImpl(context)
  }

  @Provides @ServiceScope fun provideFastBookmarker(
      clipboardManager: ClipboardManager,
      context: Context,
      packageManager: PackageManager,
      preferences: AppPreferences,
      intentFactory: IntentFactory): FastBookmarker {
    val packageManagerDelegate = PackageManagerDelegateImpl(packageManager)
    return FastBookmarker(clipboardManager, context, packageManagerDelegate, preferences,
        intentFactory)
  }
}
