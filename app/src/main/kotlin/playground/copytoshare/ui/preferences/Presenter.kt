package playground.copytoshare.ui.preferences

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.preference.Preference
import playground.copytoshare.background.boot.BootBroadcastReceiver
import playground.copytoshare.background.service.CopyToShareService

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
