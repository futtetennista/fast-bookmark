package com.stefano.playground.fastbookmark.utils

class AppPreferencesTest {

  lateinit var appPreferences: AppPreferences

  @Before
  fun setUp() {
    var preferences = mock(SharedPreferences::class.java)
    `when`(preferences.getString("pref_list_favourite_sharing_app", any()))
        .thenReturn("package.name/activityName")
    appPreferences = AppPreferences(preferences)
  }

  @Test
  fun itShouldGetFavouriteBookmarksAppData() {
    val pair = appPreferences.getFavouriteBookmarksAppData()

    assertEquals(Pair("package.name", "activityName"), pair)
  }

  @Test
  fun itShouldGetFavouriteBookmarksAppDataAsString() {
    val string = appPreferences.getFavouriteBookmarksAppDataAsString()

    assertEquals("package.name/activityName", string)
  }

  @Test
  fun itShouldMapActivityInfoToString() {
    val activityInfo = ActivityInfo()
    activityInfo.name = "activityName"
    val applicationInfo = ApplicationInfo()
    applicationInfo.packageName = "package.name"
    activityInfo.applicationInfo = applicationInfo

    assertEquals(appPreferences.toString(activityInfo), "package.name/activityName")
  }
}
