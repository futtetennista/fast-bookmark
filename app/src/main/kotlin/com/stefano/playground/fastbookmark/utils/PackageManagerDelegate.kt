package com.stefano.playground.fastbookmark.utils

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

interface PackageManagerDelegate {

  val packageManager: PackageManager

  fun retrieveIntentHandlers(intent: Intent?): MutableList<ResolveInfo>

  fun isIntentHandlerInstalled(intent: Intent?,
                               shareActivityInfo: ActivityInfo?): Boolean

  fun canAnyInstalledAppHandleIntent(chooserIntent: Intent): Boolean
}

class PackageManagerDelegateImpl(packageManager: PackageManager) : PackageManagerDelegate {
  override val packageManager: PackageManager
    get() = pm
  private var pm: PackageManager

  init {
    this.pm = packageManager
  }


  override fun retrieveIntentHandlers(intent: Intent?): MutableList<ResolveInfo> {
    return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
  }

  override fun isIntentHandlerInstalled(intent: Intent?,
                                        shareActivityInfo: ActivityInfo?): Boolean {
    val activities = retrieveIntentHandlers(intent)
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
