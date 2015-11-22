package copytoshare.ui.preferences

import copytoshare.ApplicationComponent
import copytoshare.inject.AbstractActivityComponent
import copytoshare.inject.ActivityScope
import dagger.Component

@ActivityScope
@Component(
    dependencies = arrayOf(ApplicationComponent::class),
    modules = arrayOf(ActivityModule::class)
)
interface ActivityComponent : AbstractActivityComponent {

  fun inject(activity: SettingsActivity)

  fun plus(module: FragmentModule): FragmentComponent
}