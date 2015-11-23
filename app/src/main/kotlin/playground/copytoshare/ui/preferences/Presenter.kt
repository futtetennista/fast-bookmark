package playground.copytoshare.ui.preferences

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Rect
import android.preference.Preference
import android.text.Spannable
import android.text.SpannableString
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import playground.copytoshare.background.boot.BootBroadcastReceiver
import playground.copytoshare.background.service.CopyToShareService
import java.util.*

class Presenter {

  val context: Context
  val preferences: SharedPreferences

  constructor(context: Context, preferences: SharedPreferences) {
    this.context = context
    this.preferences = preferences
  }

  fun bindView(view: SettingsView) {
    view.enableAutoSharing?.onPreferenceChangeListener = enableListener()

    val sharingAppsNamesData = retrieveSharingAppsData(context)
    if (sharingAppsNamesData.first != null) {
      view.sharingApp?.setDefaultValue(sharingAppsNamesData.first)
    }
    view.sharingApp?.entries = sharingAppsNamesData.second.first
    view.sharingApp?.entryValues = sharingAppsNamesData.second.second
  }

  private fun retrieveSharingAppsData(context: Context):
      Pair<Int?, Pair<Array<out CharSequence>, Array<out String>>> {
    val intent = Intent(Intent.ACTION_SEND).setType("text/plain")
    val activities =
        context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return retrieveSharingAppDataFromActivities(activities, context)
  }

  private fun retrieveSharingAppDataFromActivities(activities: MutableList<ResolveInfo>,
                                                   context: Context):
      Pair<Int?, Pair<Array<CharSequence>, Array<String>>> {
    val entryAndValues =
        activities.foldRight(Pair(arrayListOf<CharSequence>(), arrayListOf<String>()),
            { resolveInfo, acc ->
              val activityInfo = resolveInfo.activityInfo
              val entry = SpannableString(" " + resolveInfo.loadLabel(context.packageManager))
              val icon = activityInfo.applicationInfo.loadIcon(context.packageManager)
              icon.bounds = Rect(0, 0, icon.intrinsicWidth , icon.intrinsicHeight)
              entry.setSpan(ImageSpan(icon, DynamicDrawableSpan.ALIGN_BASELINE),
                  0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
              acc.first.add(entry)
              acc.second.add("${activityInfo.applicationInfo.packageName}/${activityInfo.name}")
              acc
            })

    return Pair(retrieveDefaultAppIndex(entryAndValues.second),
        Pair(entryAndValues.first.toTypedArray(), entryAndValues.second.toTypedArray()))
  }

  private fun retrieveDefaultAppIndex(entryValues: ArrayList<String>): Int? {
    entryValues.mapIndexed { idx, value ->
      if (value == defaultShareActivityInfo()) {
        return idx
      }
    }
    return null
  }

  private fun defaultShareActivityInfo(): String? {
    return preferences.getString("pref_list_favourite_sharing_app", null)
  }

  private fun enableListener(): Preference.OnPreferenceChangeListener {
    return Preference.OnPreferenceChangeListener { pref, newValue ->
      when (newValue as Boolean) {
        true -> {
          setComponentsEnableState(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
          CopyToShareService.start(context)
        }
        false -> {
          CopyToShareService.stop(context)
          setComponentsEnableState(PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
        }
      }
      true
    }
  }

  private fun setComponentsEnableState(enabledState: Int) {
    context.packageManager.setComponentEnabledSetting(
        ComponentName.createRelative(context, CopyToShareService::class.java.name),
        enabledState,
        0
    )
    context.packageManager.setComponentEnabledSetting(
        ComponentName.createRelative(context, BootBroadcastReceiver::class.java.name),
        enabledState,
        0
    )
  }
}
