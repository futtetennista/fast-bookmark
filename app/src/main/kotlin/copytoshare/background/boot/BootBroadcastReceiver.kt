package copytoshare.background.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import copytoshare.background.service.CopyToShareService

class BootBroadcastReceiver: BroadcastReceiver() {

  override fun onReceive(context: Context?, intent: Intent?) {
    CopyToShareService.start(context!!)
  }
}
