package copytoshare

import android.app.Service
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import playground.copytoshare.R
import java.net.MalformedURLException
import java.net.URL

class CopyToShareService: Service(), ClipboardManager.OnPrimaryClipChangedListener {

  override fun onPrimaryClipChanged() {
    if (!clipboardManager.hasPrimaryClip()) return

    val item = clipboardManager.primaryClip.getItemAt(0)
    if (isUrl(item.text.toString())) {
      applicationContext.startActivity(createShareIntent(item.text))
    }
  }

  private fun createShareIntent(link: CharSequence?): Intent? {
    val intent = Intent(Intent.ACTION_SEND)
        .setType("text/plain")
        .putExtra(Intent.EXTRA_TEXT, link)
    val chooserIntent =
        Intent.createChooser(intent, applicationContext.getString(R.string.save_link))
    chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return chooserIntent
  }

  private fun isUrl(text: String?): Boolean {
    try {
      URL(text)
    } catch (e: MalformedURLException) {
      return false
    }
    return true
  }

  override fun onCreate() {
    super.onCreate()
    clipboardManager.addPrimaryClipChangedListener(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    clipboardManager.removePrimaryClipChangedListener(this)
  }

  override fun onBind(intent: Intent?): IBinder? {
    throw UnsupportedOperationException()
  }

  private val clipboardManager: ClipboardManager
    get() {
      return applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }
}