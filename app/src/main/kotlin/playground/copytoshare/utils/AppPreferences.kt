package playground.copytoshare.utils

import android.content.SharedPreferences
import android.content.pm.ActivityInfo

class AppPreferences(preferences: SharedPreferences) {

  private var preferences: SharedPreferences

  init {
    this.preferences = preferences
  }

  fun getFavouriteBookmarksAppData(): Pair<String, String>? {
    val split = preferences.getString("pref_list_favourite_sharing_app", null)?.split("/")
    return if (split != null && split.isNotEmpty()) {
      Pair(split[0], split[1])
    } else {
      null
    }
    // return Pair("com.ideashower.readitlater.pro",
    // "com.ideashower.readitlater.activity.AddActivity")
  }

  fun getFavouriteBookmarksAppDataAsString(): String? {
    val favouriteBookmarksAppData = getFavouriteBookmarksAppData()
    return if (favouriteBookmarksAppData == null) {
      null
    } else {
      toString(favouriteBookmarksAppData.first, favouriteBookmarksAppData.second)
    }
  }

  fun toString(activityInfo: ActivityInfo): String {
    return toString(activityInfo.applicationInfo.packageName, activityInfo.name)
  }

  private fun toString(packageName: String, activityName: String): String {
    return "$packageName/$activityName"
  }
}