package com.stefano.playground.fastbookmark.ui.preferences

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.preference.Preference
import com.stefano.playground.fastbookmark.background.boot.BootBroadcastReceiver
import com.stefano.playground.fastbookmark.background.service.FastBookmarkService

class Presenter(context: Context) {

  val context: Context

  init {
    this.context = context
  }

  fun bindView(view: SettingsView) {
    view.enableAutoSharing?.onPreferenceChangeListener = enableListener()
  }

  private fun enableListener(): Preference.OnPreferenceChangeListener {
    return Preference.OnPreferenceChangeListener { pref, newValue ->
      when (newValue as Boolean) {
        true -> {
          setComponentsEnableState(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
          FastBookmarkService.start(context)
        }
        false -> {
          FastBookmarkService.stop(context)
          setComponentsEnableState(PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
        }
      }
      true
    }
  }

  private fun setComponentsEnableState(enabledState: Int) {
    context.packageManager.setComponentEnabledSetting(
        ComponentName(context, FastBookmarkService::class.java.name),
        enabledState,
        PackageManager.DONT_KILL_APP
    )
    context.packageManager.setComponentEnabledSetting(
        ComponentName(context, BootBroadcastReceiver::class.java.name),
        enabledState,
        PackageManager.DONT_KILL_APP
    )
  }
}
