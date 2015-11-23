package com.stefano.playground.fastbookmark

import android.app.Application

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

    com.stefano.playground.fastbookmark.background.service.FastBookmarkService.Companion.start(this)
  }
}
