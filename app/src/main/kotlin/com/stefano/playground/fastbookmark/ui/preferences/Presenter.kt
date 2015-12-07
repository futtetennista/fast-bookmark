package com.stefano.playground.fastbookmark.ui.preferences

import android.content.Context
import android.content.pm.PackageManager
import android.preference.Preference
import com.stefano.playground.fastbookmark.background.boot.BootBroadcastReceiver
import com.stefano.playground.fastbookmark.background.service.FastBookmarkService

open class Presenter(context: Context, packageManager: PackageManager, factory: AndroidFactory) {

  val context: Context
  val packageManager: PackageManager
  val factory: AndroidFactory

  init {
    this.context = context
    this.packageManager = packageManager
    this.factory = factory
  }

  fun bindView(view: SettingsView) {
    view.enableAutoSharing?.onPreferenceChangeListener = enableListener()
  }

  private fun enableListener(): Preference.OnPreferenceChangeListener {
    return Preference.OnPreferenceChangeListener { pref, newValue ->
      changeManifestComponentsEnableState(newValue as Boolean)
    }
  }

  fun changeManifestComponentsEnableState(enable: Boolean?): Boolean {
    when (enable) {
      true -> {
        setComponentsEnableState(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
        FastBookmarkService.start(context)
      }
      false -> {
        FastBookmarkService.stop(context)
        setComponentsEnableState(PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
      }
    }
    return true
  }

  private fun setComponentsEnableState(enabledState: Int) {
    packageManager.setComponentEnabledSetting(
        factory.createComponentName(context, FastBookmarkService::class.java.name),
        enabledState,
        PackageManager.DONT_KILL_APP
    )
    packageManager.setComponentEnabledSetting(
        factory.createComponentName(context, BootBroadcastReceiver::class.java.name),
        enabledState,
        PackageManager.DONT_KILL_APP
    )
  }
}
