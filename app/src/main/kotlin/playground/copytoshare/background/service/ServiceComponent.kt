package playground.copytoshare.background.service

import dagger.Component
import playground.copytoshare.ApplicationComponent
import playground.copytoshare.inject.ServiceScope

@ServiceScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ServiceComponent {

  fun inject(service: CopyToShareService)
}