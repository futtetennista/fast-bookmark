package copytoshare

import android.app.Application
import copytoshare.inject.ApplicationComponent
import copytoshare.inject.ApplicationModule
import copytoshare.inject.DaggerApplicationComponent

class CopyToShareApplication : Application() {

  companion object {
    //platformStatic allow access it from java code
    @JvmStatic lateinit var objectGraph: ApplicationComponent
  }

  override fun onCreate() {
    super.onCreate()
    objectGraph =
        DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    objectGraph.inject(this)

    CopyToShareService.start(this)
  }
}
