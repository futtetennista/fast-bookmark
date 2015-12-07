package com.stefano.playground.fastbookmark.background.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.stefano.playground.fastbookmark.FastBookmarkApplication
import javax.inject.Inject

class FastBookmarkService : Service() {

  @Inject lateinit var fastBookmarker: FastBookmarker

  override fun onCreate() {
    super.onCreate()
    DaggerServiceComponent
        .builder()
        .applicationComponent(FastBookmarkApplication.component)
        .serviceModule(ServiceModule())
        .build()
        .inject(this)
    fastBookmarker.activate()
  }

  override fun onDestroy() {
    super.onDestroy()
    fastBookmarker.deactivate()
  }

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  companion object {
    fun start(context: Context) {
//      Log.i("FastBookmarkService", "starting…")
      context.startService(Intent(context, FastBookmarkService::class.java))
    }

    fun stop(context: Context) {
//      Log.i("FastBookmarkService", "stopping…")
      context.stopService(Intent(context, FastBookmarkService::class.java))
    }
  }
}