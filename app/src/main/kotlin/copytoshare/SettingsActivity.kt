package copytoshare


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import playground.copytoshare.R
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (savedInstanceState == null) {
      fragmentManager
          .beginTransaction()
          .add(android.R.id.content, GeneralPreferenceFragment())
          .commit()
    }
    //    val flags = Intent::class.java.fields.filter { f ->
    //      f.name.startsWith("FLAG_")
    //    }.map { f ->
    //      Pair(f.name, f.getInt(intent))
    //    }
    //    val setFlagNames = flags.filter { pair ->
    //      (intent.flags and(1 shl(pair.second))) != 0
    //    }.map { pair ->
    //      pair.first
    //    }
    //    Log.i("INTENT FLAGS COUNT", setFlagNames.size.toString())
    //    for (name in setFlagNames)
    //      Log.i("INTENT FLAG SET", name)
  }

  /**
   * Set up the [android.app.ActionBar], if the API is available.
   */
  private fun setupActionBar() {
    val actionBar = supportActionBar
    actionBar.setDisplayHomeAsUpEnabled(true)
  }

  /**
   * This fragment shows general preferences only. It is used when the
   * activity is showing a two-pane settings UI.
   */
  class GeneralPreferenceFragment : PreferenceFragment() {

    @Inject lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      CopyToShareApplication.objectGraph.inject(this)
      addPreferencesFromResource(R.xml.pref_general)
      setHasOptionsMenu(true)

      //      bindPreferenceSummaryToValue(findPreference("pref_enable"))
      findPreference("pref_switch_enable").onPreferenceChangeListener =
          Preference.OnPreferenceChangeListener { pref, newValue ->
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
      val defaultAppPreference = findPreference("pref_list_apps") as ListPreference
      defaultAppPreference.onPreferenceChangeListener =
          Preference.OnPreferenceChangeListener { pref, newSharingActivityName ->
            var sharingActivityPackageName =
                retrieveSharingActivityPackageName(context, newSharingActivityName as String)
            preferences.edit()
                .putString("sharing_activity_package", sharingActivityPackageName)
                .putString("sharing_activity_name", newSharingActivityName)
                .apply()
            false
          }
      val sharingAppsNamesData = retrieveSharingAppsData(activity)
      if (sharingAppsNamesData.first != null) {
        defaultAppPreference.setDefaultValue(sharingAppsNamesData.first)
      }
      defaultAppPreference.entries = sharingAppsNamesData.second
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

    private fun retrieveSharingActivityPackageName(context: Context,
                                                   newSharingActivityName: String):
        String? {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      val id = item.itemId
      if (id == android.R.id.home) {
        startActivity(Intent(activity, SettingsActivity::class.java))
        return true
      }
      return super.onOptionsItemSelected(item)
    }
  }
}
