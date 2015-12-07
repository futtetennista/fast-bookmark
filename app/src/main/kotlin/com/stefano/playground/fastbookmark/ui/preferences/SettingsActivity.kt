package com.stefano.playground.fastbookmark.ui.preferences


import android.content.Intent
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.view.MenuItem
import android.view.View
import com.stefano.playground.fastbookmark.FastBookmarkApplication
import com.stefano.playground.fastbookmark.R
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

  lateinit var component: ActivityComponent

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    component =
        DaggerActivityComponent
            .builder()
            .applicationComponent(FastBookmarkApplication.component)
            .activityModule(ActivityModule(this))
            .build()

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
  class GeneralPreferenceFragment : PreferenceFragment(), SettingsView {
    override val sharingApp: ListPreference?
      get() = findPreference("pref_list_favourite_sharing_app") as ListPreference
    override val enableAutoSharing: Preference?
      get() = findPreference("pref_switch_enable")

    @Inject lateinit var presenter: Presenter
    @Inject lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      (activity as SettingsActivity).component.plus(FragmentModule()).inject(this)

      addPreferencesFromResource(R.xml.pref_general)
      setHasOptionsMenu(true)
      retainInstance = true
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      sharingApp?.entries = viewModel.entries
      sharingApp?.entryValues = viewModel.entryValues
      sharingApp?.setDefaultValue(viewModel.defaultEntryIndex)
      presenter.bindView(this)
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
