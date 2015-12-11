package com.stefano.playground.fastbookmark.utils

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import kotlin.test.assertEquals

@SmallTest
@RunWith(AndroidJUnit4::class)
class AppPreferencesTest {

  lateinit var appPreferences: AppPreferences

  @Before
  fun setUp() {
    var preferences = mock(SharedPreferences::class.java)
    `when`(preferences.getString(eq("pref_list_favourite_sharing_app"), anyString()))
        .thenReturn("package.name/ActivityName")
    appPreferences = AppPreferences(preferences)
  }

  @Test
  fun itShouldGetFavouriteBookmarksAppData() {
    val pair = appPreferences.getFavouriteBookmarksAppData()

    assertEquals(Pair("package.name", "ActivityName"), pair)
  }

  @Test
  fun itShouldGetFavouriteBookmarksAppDataAsString() {
    val string = appPreferences.getFavouriteBookmarksAppDataAsString()

    assertEquals("package.name/ActivityName", string)
  }

  @Test
  fun itShouldMapActivityInfoToString() {
    val activityInfo = ActivityInfo()
    activityInfo.name = "ActivityName"
    val applicationInfo = ApplicationInfo()
    applicationInfo.packageName = "package.name"
    activityInfo.applicationInfo = applicationInfo

    assertEquals(appPreferences.toString(activityInfo), "package.name/ActivityName")
  }
}
