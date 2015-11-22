package copytoshare.ui.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FragmentModule {

  @Provides @Singleton fun providePresenter(context: Context,
                                            preferences: SharedPreferences): Presenter {
    return Presenter(context, preferences)
  }
}