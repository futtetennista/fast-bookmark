package copytoshare.ui.preferences

import dagger.Component
import dagger.Subcomponent

@Subcomponent
@Component(
    modules = arrayOf(FragmentModule::class)
)
interface FragmentComponent {

  fun inject(fragment: SettingsActivity.GeneralPreferenceFragment)
}