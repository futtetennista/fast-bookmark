package playground.copytoshare.ui.preferences

import dagger.Component
import playground.copytoshare.ApplicationComponent
import playground.copytoshare.inject.AbstractActivityComponent
import playground.copytoshare.inject.ActivityScope

@ActivityScope
@Component(
    dependencies = arrayOf(ApplicationComponent::class),
    modules = arrayOf(ActivityModule::class)
)
interface ActivityComponent : AbstractActivityComponent {

  fun inject(activity: SettingsActivity)

  fun plus(module: FragmentModule): FragmentComponent
}
