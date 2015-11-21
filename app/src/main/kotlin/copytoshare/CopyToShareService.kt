package copytoshare

import android.app.Service
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager.MATCH_DEFAULT_ONLY
import android.os.IBinder
import playground.copytoshare.R
import java.net.MalformedURLException
import java.net.URL

class CopyToShareService : Service(), ClipboardManager.OnPrimaryClipChangedListener {

  override fun onPrimaryClipChanged() {
    if (!clipboardManager.hasPrimaryClip()) return

    val item = clipboardManager.primaryClip.getItemAt(0)
    if (isUrl(item.text.toString())) {
      val baseIntent = Intent(Intent.ACTION_SEND)
          .setType("text/plain")
          .putExtra(Intent.EXTRA_TEXT, item.text)
      val intent = favouriteBookmarksAppOrChooserIntent(baseIntent)
      if (intent != null) {
        applicationContext.startActivity(intent)
      }
    }
  }

  private fun favouriteBookmarksAppOrChooserIntent(intent: Intent): Intent? {
    val info = retrieveFavouriteBookmarksAppInfo(intent)
    if (info != null) {
      return Intent(intent).setClassName(info.first, info.second)
          .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    } else {
      val chooserIntent =
          Intent.createChooser(intent, applicationContext.getString(R.string.save_link))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      return if (chooserIntent.resolveActivity(packageManager) != null) {
        chooserIntent
      } else {
        null
      }
    }
  }

  private fun retrieveFavouriteBookmarksAppInfo(intent: Intent): Pair<String, String>? {
    val info = retrieveStoredFavouriteBookmarksAppInfo()
    return if (favouriteBookmarksAppInstalled(intent, info)) {
      info
    } else {
      null
    }
  }

  private fun favouriteBookmarksAppInstalled(intent: Intent,
                                             shareActivityInfo: Pair<String, String>)
      : Boolean {
    val activities =
        applicationContext.packageManager.queryIntentActivities(intent, MATCH_DEFAULT_ONLY)
    val res = activities.filter { a ->
      a.activityInfo.packageName == shareActivityInfo.first
          && a.activityInfo.name == shareActivityInfo.second
    }
    return res.isNotEmpty()
  }

  private fun retrieveStoredFavouriteBookmarksAppInfo(): Pair<String, String> {
    return Pair("com.ideashower.readitlater.pro", "com.ideashower.readitlater.activity.AddActivity")
  }

  // TODO: is there a cleaner way to do this that doesn't involve regex?
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