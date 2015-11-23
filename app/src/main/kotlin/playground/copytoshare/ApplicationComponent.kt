package playground.copytoshare

import android.content.Context
import dagger.Component
import playground.copytoshare.utils.AppPreferences
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

  fun inject(application: CopyToShareApplication)

  // Expose to the graph
  fun context(): Context
  fun preferences(): AppPreferences
}
