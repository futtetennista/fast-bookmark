package com.stefano.playground.fastbookmark.ui.preferences

import android.preference.ListPreference
import android.preference.Preference

interface SettingsView {
  val enableAutoSharing: Preference?
  val sharingApp: ListPreference?
}