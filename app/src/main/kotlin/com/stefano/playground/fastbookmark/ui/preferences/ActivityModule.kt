package com.stefano.playground.fastbookmark.ui.preferences

import android.app.Activity
import com.stefano.playground.fastbookmark.inject.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

  private var activity: SettingsActivity

  constructor(activity: SettingsActivity) {
    this.activity = activity
  }

  @Provides @ActivityScope fun provideActivity(): Activity {
    return activity
  }
}
