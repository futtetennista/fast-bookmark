package playground.copytoshare.ui.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

  @Provides fun providePresenter(context: Context): Presenter {
    return Presenter(context)
  }

  @Provides fun provideViewModel(context: Context,
                                 preferences: SharedPreferences): ViewModel {
    return ViewModel(context, preferences)
  }
}
