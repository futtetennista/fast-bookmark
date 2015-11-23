package com.stefano.playground.fastbookmark.ui.preferences

import com.stefano.playground.fastbookmark.ApplicationComponent
import com.stefano.playground.fastbookmark.inject.AbstractActivityComponent
import com.stefano.playground.fastbookmark.inject.ActivityScope
import dagger.Component

@ActivityScope
@Component(
    dependencies = arrayOf(ApplicationComponent::class),
    modules = arrayOf(ActivityModule::class)
)
interface ActivityComponent : AbstractActivityComponent {

  fun inject(activity: SettingsActivity)

  fun plus(module: FragmentModule): FragmentComponent
}
