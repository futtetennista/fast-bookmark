package copytoshare.ui.preferences

import android.preference.ListPreference
import android.preference.Preference

interface SettingsView {
  val enable: Preference?
  val sharingApp: ListPreference?
}