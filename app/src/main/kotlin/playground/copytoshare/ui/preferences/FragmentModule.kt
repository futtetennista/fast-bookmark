package playground.copytoshare.ui.preferences

import android.content.Context
import dagger.Module
import dagger.Provides
import playground.copytoshare.utils.AppPreferences

@Module
class FragmentModule {

  @Provides fun providePresenter(context: Context): Presenter {
    return Presenter(context)
  }

  @Provides fun provideViewModel(context: Context,
                                 preferences: AppPreferences): ViewModel {
    return ViewModel(context, preferences)
  }
}
