package com.stefano.playground.fastbookmark.utils

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager

interface PackageManagerDelegate {

  fun isIntentHandlerInstalled(intent: Intent?,
                               shareActivityInfo: ActivityInfo?): Boolean

  fun canAnyInstalledAppHandleIntent(chooserIntent: Intent): Boolean
}

class PackageManagerDelegateImpl(packageManager: PackageManager) : PackageManagerDelegate {

  private var packageManager: PackageManager

  init {
    this.packageManager = packageManager
  }

  override fun isIntentHandlerInstalled(intent: Intent?,
                                        shareActivityInfo: ActivityInfo?): Boolean {
    val activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    val res = activities.filter { a ->
      a.activityInfo.packageName == shareActivityInfo?.packageName
          && a.activityInfo.name == shareActivityInfo?.name
    }
    return res.isNotEmpty()
  }

  override fun canAnyInstalledAppHandleIntent(chooserIntent: Intent): Boolean {
    return chooserIntent.resolveActivity(packageManager) != null
  }
}
