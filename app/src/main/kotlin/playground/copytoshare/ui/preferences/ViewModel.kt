package playground.copytoshare.ui.preferences

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Rect
import android.text.Spannable
import android.text.SpannableString
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan

class ViewModel(context: Context, preferences: SharedPreferences) {

  val defaultValue: Int?

  val entries: Array<out CharSequence>?

  val entryValues: Array<out CharSequence>?

  init {
    val data = retrieveSharingAppsData(context)
    entries = data.first
    entryValues = data.second
    defaultValue = retrieveDefaultAppIndex(preferences, entryValues)
  }

  private fun retrieveSharingAppsData(context: Context):
      Pair<Array<out CharSequence>, Array<out String>> {
    val intent = Intent(Intent.ACTION_SEND).setType("text/plain")
    val activities =
        context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return retrieveSharingAppDataFromActivities(activities, context)
  }

  private fun retrieveSharingAppDataFromActivities(activities: MutableList<ResolveInfo>,
                                                   context: Context):
      Pair<Array<CharSequence>, Array<String>> {
    val entryAndValues =
        activities.foldRight(Pair(arrayListOf<CharSequence>(), arrayListOf<String>()),
            { resolveInfo, acc ->
              val activityInfo = resolveInfo.activityInfo
              val entry = SpannableString(" " + resolveInfo.loadLabel(context.packageManager))
              val icon = activityInfo.applicationInfo.loadIcon(context.packageManager)
              icon.bounds = Rect(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
              entry.setSpan(ImageSpan(icon, DynamicDrawableSpan.ALIGN_BASELINE),
                  0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
              acc.first.add(entry)
              acc.second.add("${activityInfo.applicationInfo.packageName}/${activityInfo.name}")
              acc
            })

    return Pair(entryAndValues.first.toTypedArray(), entryAndValues.second.toTypedArray())
  }

  private fun retrieveDefaultAppIndex(preferences: SharedPreferences,
                                      entryValues: Array<out CharSequence>?): Int? {
    entryValues?.toArrayList()?.mapIndexed { idx, value ->
      if (value == defaultShareActivityInfo(preferences)) {
        return idx
      }
    }
    return null
  }

  private fun defaultShareActivityInfo(preferences: SharedPreferences): String? {
    return preferences.getString("pref_list_favourite_sharing_app", null)
  }
}