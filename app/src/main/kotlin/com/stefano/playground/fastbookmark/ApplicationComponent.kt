package com.stefano.playground.fastbookmark

import android.content.Context
import com.stefano.playground.fastbookmark.utils.AppPreferences
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

  fun inject(application: FastBookmarkApplication)

  // Expose to the graph
  fun context(): Context
  fun preferences(): AppPreferences
}
