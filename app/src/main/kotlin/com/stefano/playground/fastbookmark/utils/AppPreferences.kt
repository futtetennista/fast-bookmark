package com.stefano.playground.fastbookmark.utils

import android.content.SharedPreferences
import android.content.pm.ActivityInfo

interface AppPreferences {
  fun getFavouriteBookmarks(): ActivityInfo?
  fun getFavouriteBookmarksAsString(): String?
  fun toString(activityInfo: ActivityInfo?): String?
}

class AppPreferencesImpl(preferences: SharedPreferences): AppPreferences {

  private val delimiter = "/"
  private var preferences: SharedPreferences

  init {
    this.preferences = preferences
  }

  override fun getFavouriteBookmarks(): ActivityInfo? {
    val tokens = preferences.getString("pref_list_favourite_sharing_app", null)?.split(delimiter)
    return if (tokens != null && tokens.isNotEmpty()) {
      val info = ActivityInfo()
      info.packageName = tokens.first()
      info.name = tokens.last()
      return info
    } else {
      null
    }
  }

  override fun getFavouriteBookmarksAsString(): String? {
    val favouriteBookmarksAppData = getFavouriteBookmarks()
    return if (favouriteBookmarksAppData == null) {
      null
    } else {
      toString(favouriteBookmarksAppData.packageName, favouriteBookmarksAppData.name)
    }
  }

  override fun toString(activityInfo: ActivityInfo?): String? {
    return if (activityInfo == null) {
      null
    } else {
      toString(activityInfo.packageName, activityInfo.name)
    }
  }

  private fun toString(packageName: String, activityName: String): String {
    return "$packageName$delimiter$activityName"
  }
}