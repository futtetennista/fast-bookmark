package copytoshare

import android.app.Application
import copytoshare.background.service.CopyToShareService

class CopyToShareApplication : Application() {

  companion object {
    lateinit var component: ApplicationComponent
  }

  override fun onCreate() {
    super.onCreate()
    component =
        DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    component.inject(this)

    CopyToShareService.start(this)
  }
}
