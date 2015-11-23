package playground.copytoshare.ui.preferences

import dagger.Subcomponent

@Subcomponent(
    modules = arrayOf(FragmentModule::class)
)
interface FragmentComponent {

  fun inject(fragment: SettingsActivity.GeneralPreferenceFragment)
}
