package copytoshare

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

  fun inject(application: CopyToShareApplication)

  // Expose preferences to child-components
  fun context(): Context
  fun preferences(): SharedPreferences
}
