package copytoshare

import android.app.Application
import android.content.Intent

class CopyToShareApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    startService(Intent(this, CopyToShareService::class.java))
  }
}
