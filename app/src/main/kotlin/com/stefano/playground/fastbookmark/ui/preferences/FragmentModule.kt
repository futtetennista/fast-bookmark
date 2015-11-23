package com.stefano.playground.fastbookmark.ui.preferences

import android.content.Context
import com.stefano.playground.fastbookmark.utils.AppPreferences
import dagger.Module
import dagger.Provides

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
