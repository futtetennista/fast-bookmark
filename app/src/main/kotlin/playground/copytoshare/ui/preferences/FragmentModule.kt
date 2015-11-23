package playground.copytoshare.ui.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

  @Provides fun providePresenter(context: Context,
                                 preferences: SharedPreferences): Presenter {
    return Presenter(context, preferences)
  }
}
