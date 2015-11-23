package com.stefano.playground.fastbookmark.ui.preferences

import dagger.Subcomponent

@Subcomponent(
    modules = arrayOf(FragmentModule::class)
)
interface FragmentComponent {

  fun inject(fragment: SettingsActivity.GeneralPreferenceFragment)
}
