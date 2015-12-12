package com.stefano.playground.fastbookmark.ui.preferences

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.text.SpannableString
import com.stefano.playground.fastbookmark.utils.AppPreferences

class ViewModel(packageManager: PackageManager, preferences: AppPreferences) {

  val defaultEntryIndex: Int?
  val entries: Array<out CharSequence>?
  val entryValues: Array<out CharSequence?>?

  init {
    val data = retrieveSharingAppsData(packageManager, preferences)
    entries = data.first
    entryValues = data.second
    defaultEntryIndex = retrieveDefaultAppIndex(preferences, entryValues)
  }

  private fun retrieveSharingAppsData(packageManager: PackageManager,
                                      preferences: AppPreferences):
      Pair<Array<out CharSequence>, Array<out String?>> {
    val intent = Intent(Intent.ACTION_SEND).setType("text/plain")
    val activities =
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return retrieveSharingAppDataFromActivities(activities, packageManager, preferences)
  }

  private fun retrieveSharingAppDataFromActivities(activities: MutableList<ResolveInfo>,
                                                   packageManager: PackageManager,
                                                   preferences: AppPreferences):
      Pair<Array<CharSequence>, Array<String?>> {
    val entryAndValues =
        activities.foldRight(Pair(arrayListOf<CharSequence>(), arrayListOf<String?>()),
            { resolveInfo, acc ->
              val activityInfo = resolveInfo.activityInfo
              // TODO: fix appearance
              val entry = SpannableString(resolveInfo.loadLabel(packageManager))
              //              val icon = activityInfo.applicationInfo.loadIcon(packageManager)
              //              icon.bounds = Rect(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
              //              entry.setSpan(ImageSpan(icon, DynamicDrawableSpan.ALIGN_BASELINE),
              //                  0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
              acc.first.add(entry)
              acc.second.add(preferences.toString(activityInfo))
              acc
            })

    return Pair(entryAndValues.first.toTypedArray(), entryAndValues.second.toTypedArray())
  }

  private fun retrieveDefaultAppIndex(preferences: AppPreferences,
                                      entryValues: Array<out CharSequence?>?): Int? {
    val favouriteBookmarksAppData =
        preferences.getFavouriteBookmarksAsString() ?: return null

    entryValues?.toArrayList()?.mapIndexed { idx, value ->
      if (value == favouriteBookmarksAppData) {
        return idx
      }
    }
    return null
  }
}