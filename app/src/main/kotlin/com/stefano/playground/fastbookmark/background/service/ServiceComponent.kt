package com.stefano.playground.fastbookmark.background.service

import com.stefano.playground.fastbookmark.ApplicationComponent
import com.stefano.playground.fastbookmark.inject.ServiceScope
import dagger.Component

@ServiceScope
@Component(
    dependencies = arrayOf(ApplicationComponent::class),
    modules = arrayOf(ServiceModule::class)
    )
interface ServiceComponent {

  fun inject(service: FastBookmarkService)
}