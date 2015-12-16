package com.stefano.playground.fastbookmark.ui.preferences

import android.content.Context
import android.content.pm.PackageManager
import com.stefano.playground.fastbookmark.utils.AppPreferences
import com.stefano.playground.fastbookmark.utils.PackageManagerDelegateImpl
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

  @Provides fun providePresenter(context: Context,
                                 packageManager: PackageManager,
                                 factory: ComponentNameFactory): Presenter {
    return Presenter(context, packageManager, factory)
  }

  @Provides fun provideViewModel(packageManager: PackageManager,
                                 preferences: AppPreferences): ViewModel {
    return ViewModel(PackageManagerDelegateImpl(packageManager), preferences)
  }

  @Provides fun provideComponentNameFactory(): ComponentNameFactory {
    return ComponentNameFactoryImpl()
  }
}
