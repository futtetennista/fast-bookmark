package copytoshare.inject

import copytoshare.CopyToShareApplication
import copytoshare.SettingsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

  fun inject(application: CopyToShareApplication)
  fun inject(preferenceFragment: SettingsActivity.GeneralPreferenceFragment)
}
