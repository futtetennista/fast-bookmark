package com.stefano.playground.fastbookmark.background.service

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import com.stefano.playground.fastbookmark.R
import com.stefano.playground.fastbookmark.ui.preferences.SettingsActivity

// Test-induced damage? This is useful so that I don't have to spy on the test subject
interface IntentFactory {

  fun createFavouriteBookmarksAppIntent(item: ClipData.Item?,
                                        shareActivityInfo: ActivityInfo?): Intent?
  fun createShareIntent(item: ClipData.Item?): Intent?
  fun createChooserIntent(item: ClipData.Item?): Intent?
  fun createAppIntent(): Intent
}

class IntentFactoryImpl(applicationContext: Context) : IntentFactory {

  private var applicationContext: Context

  init {
    this.applicationContext = applicationContext
  }

  override fun createFavouriteBookmarksAppIntent(item: ClipData.Item?,
                                                 shareActivityInfo: ActivityInfo?): Intent? {
    val intent = createShareIntent(item)

    return if (item == null || shareActivityInfo == null){
      Intent(intent)
          .setClassName(shareActivityInfo!!.packageName, shareActivityInfo.name)
          .setFlags(CHOOSER_ITEM_FLAGS)
    } else {
      null
    }
  }

  override fun createShareIntent(item: ClipData.Item?): Intent? {
    return if (item == null) {
      null
    } else {
      Intent(Intent.ACTION_SEND)
          .setType("text/plain")
          .putExtra(Intent.EXTRA_TEXT, item.text)
    }
  }

  override fun createChooserIntent(item: ClipData.Item?): Intent? {
    val text = applicationContext.getString(R.string.save_link)
    return Intent.createChooser(createShareIntent(item!!), text)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

  }

  override fun createAppIntent(): Intent {
   return Intent(applicationContext, SettingsActivity::class.java)
  }

  companion object {

    private val CHOOSER_ITEM_FLAGS: Int =
        (Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            or(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            or(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            or(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            or(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
            or(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            or(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            or(Intent.FLAG_ACTIVITY_NEW_TASK)
            or(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            or(Intent.FLAG_ACTIVITY_NO_HISTORY)
            or(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            or(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            or(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            or(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS)
            or(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            //          or(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
            //          or(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            //          or(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
            //          or(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            //          or(Intent.FLAG_RECEIVER_FOREGROUND)
            //          or(Intent.FLAG_RECEIVER_NO_ABORT)
            //          or(Intent.FLAG_RECEIVER_REGISTERED_ONLY)
            //          or(Intent.FLAG_RECEIVER_REPLACE_PENDING)
            )
  }
}
