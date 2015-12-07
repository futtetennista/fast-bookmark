package com.stefano.playground.fastbookmark

import android.app.Application
import com.stefano.playground.fastbookmark.background.service.FastBookmarkService

class FastBookmarkApplication : Application() {

  companion object {
    lateinit var component: ApplicationComponent
  }

  override fun onCreate() {
    super.onCreate()
    component =
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    component.inject(this)

    FastBookmarkService.start(this)
  }
}
