package playground.copytoshare.ui.preferences

import android.app.Activity
import dagger.Module
import dagger.Provides
import playground.copytoshare.inject.ActivityScope

@Module
class ActivityModule {

  private var activity: SettingsActivity

  constructor(activity: SettingsActivity) {
    this.activity = activity
  }

  @Provides @ActivityScope fun provideActivity(): Activity {
    return activity
  }
}
