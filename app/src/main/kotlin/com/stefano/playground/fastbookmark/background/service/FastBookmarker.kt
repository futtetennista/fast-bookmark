package com.stefano.playground.fastbookmark.background.service

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.widget.Toast
import com.stefano.playground.fastbookmark.R
import com.stefano.playground.fastbookmark.utils.AppPreferences
import com.stefano.playground.fastbookmark.utils.PackageManagerDelegate
import java.net.MalformedURLException
import java.net.URL

class FastBookmarker(clipboardManager: ClipboardManager,
                     applicationContext: Context,
                     packageManagerDelegate: PackageManagerDelegate,
                     preferences: AppPreferences,
                     intentFactory: IntentFactory) : ClipboardManager.OnPrimaryClipChangedListener {

  private val clipboardManager: ClipboardManager
  private val applicationContext: Context
  private val packageManagerDelegate: PackageManagerDelegate
  private val preferences: AppPreferences
  private val intentFactory: IntentFactory
  // Test only
  var isTestRun: Boolean = false

  init {
    this.clipboardManager = clipboardManager
    this.applicationContext = applicationContext
    this.packageManagerDelegate = packageManagerDelegate
    this.preferences = preferences
    this.intentFactory = intentFactory
  }

  fun activate() {
    clipboardManager.addPrimaryClipChangedListener(this)
  }

  fun deactivate() {
    clipboardManager.removePrimaryClipChangedListener(this)
  }

  override fun onPrimaryClipChanged() {
    if (!clipboardManager.hasPrimaryClip()) return

    val item = clipboardManager.primaryClip.getItemAt(0)
    if (isUrl(item.text.toString())) {
      val intent = toIntent(item)
      if (intent != null) {
        applicationContext.startActivity(intent)
      } else {
        applicationContext.startActivity(intentFactory.createAppIntent())
        if (!isTestRun) {
          // Creating a new collaborator just for this one liner seemed a bit over the top
          Toast.makeText(applicationContext, R.string.choose_favourite_app, Toast.LENGTH_SHORT)
              .show()
        }
      }
    }
  }

  private fun toIntent(item: ClipData.Item): Intent? {
    val shareActivityInfo =
        retrieveFavouriteBookmarksAppInfo(intentFactory.createShareIntent(item)!!)
    return if (shareActivityInfo != null) {
      intentFactory.createFavouriteBookmarksAppIntent(item, shareActivityInfo)
    } else {
      val chooserIntent = intentFactory.createChooserIntent(item)
      if (chooserIntent != null
          && packageManagerDelegate.canAnyInstalledAppHandleIntent(chooserIntent)) {
        chooserIntent
      } else {
        null
      }
    }
  }

  private fun retrieveFavouriteBookmarksAppInfo(intent: Intent): ActivityInfo? {
    val info = preferences.getFavouriteBookmarks()
    return if (packageManagerDelegate.isIntentHandlerInstalled(intent, info)) {
      info
    } else {
      null
    }
  }

  // TODO: is there a cleaner way to do this that doesn't involve crazy regexes?
  private fun isUrl(text: String?): Boolean {
    try {
      URL(text)
    } catch (e: MalformedURLException) {
      return false
    }
    return true
  }
}