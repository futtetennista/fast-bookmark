package copytoshare

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootBroadcastReceiver: BroadcastReceiver() {

  override fun onReceive(context: Context?, intent: Intent?) {
    CopyToShareService.start(context!!)
  }
}
