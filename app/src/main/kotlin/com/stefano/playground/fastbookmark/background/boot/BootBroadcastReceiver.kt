package com.stefano.playground.fastbookmark.background.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.stefano.playground.fastbookmark.background.service.FastBookmarkService

class BootBroadcastReceiver: BroadcastReceiver() {

  override fun onReceive(context: Context?, intent: Intent?) {
    FastBookmarkService.start(context!!)
  }
}
