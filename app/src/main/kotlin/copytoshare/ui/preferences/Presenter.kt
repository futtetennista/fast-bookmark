package copytoshare.ui.preferences

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.preference.Preference
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import copytoshare.background.boot.BootBroadcastReceiver
import copytoshare.background.service.CopyToShareService

class Presenter {

  val context: Context
  val preferences: SharedPreferences

  constructor(context: Context, preferences: SharedPreferences) {
    this.context = context
    this.preferences = preferences
  }

  fun bindView(view: SettingsView) {
    view.enable?.onPreferenceChangeListener = enableListener()
    view.sharingApp?.onPreferenceChangeListener = sharingAppListener()

    val sharingAppsNamesData = retrieveSharingAppsData(context)
    if (sharingAppsNamesData.first != null) {
      view.sharingApp?.setDefaultValue(sharingAppsNamesData.first)
    }
    view.sharingApp?.entries = sharingAppsNamesData.second
  }

  private fun sharingAppListener(): Preference.OnPreferenceChangeListener {
    return Preference.OnPreferenceChangeListener { pref, newSharingActivityName ->
      var sharingActivityPackageName =
          retrieveSharingActivityPackageName(context, newSharingActivityName as String)
      preferences.edit()
          .putString("sharing_activity_package", sharingActivityPackageName)
          .putString("sharing_activity_name", newSharingActivityName)
          .apply()
      false
    }
  }

  private fun retrieveSharingAppsData(context: Context): Pair<Int?, Array<out CharSequence>> {
    val intent = Intent(Intent.ACTION_SEND).setType("text/plain")
    val activities =
        context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    var defaultAppIndex: Int? = null
    val names = activities.mapIndexed { i, resolveInfo ->
      val activityInfo = resolveInfo.activityInfo
      val entry = SpannableString(activityInfo.applicationInfo.name)
      if (activityInfo.name == defaultShareActivityName()) {
        entry.setSpan(StyleSpan(Typeface.BOLD), 0, entry.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        defaultAppIndex = i
      }
      entry.setSpan(ImageSpan(context, activityInfo.applicationInfo.icon), 0, 1,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
      entry
    }.toTypedArray()

    return Pair(defaultAppIndex, names)
  }

  private fun defaultShareActivityName(): String? {
    return null
  }

  private fun retrieveSharingActivityPackageName(context: Context,
                                                 newSharingActivityName: String): String? {
    val intent = Intent(Intent.ACTION_SEND).setType("text/plain")
    val packageNames =
        context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            .filter { it.activityInfo.name == newSharingActivityName }
            .map { it.activityInfo.packageName }
    return if (packageNames.isNotEmpty()) {
      packageNames.first()
    } else {
      null
    }
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
      preferences.edit().putBoolean("enabled", newValue).apply()
      false
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
