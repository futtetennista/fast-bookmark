package com.stefano.playground.fastbookmark.utils

@SmallTest
@RunWith(AndroidJUnit4::class)
class AppPreferencesTest {

  lateinit var appPreferences: AppPreferences
  val activityInfo = ActivityInfo()

  @Before
  fun setUp() {
    activityInfo.name = "ActivityName"
    activityInfo.packageName = "package.name"
    var preferences = mock(SharedPreferences::class.java)
    `when`(preferences.getString(eq("pref_list_favourite_sharing_app"), anyString()))
        .thenReturn("package.name/ActivityName")
    appPreferences = AppPreferencesImpl(preferences)
  }

  @Test
  fun itShouldGetFavouriteBookmarks() {
    val info = appPreferences.getFavouriteBookmarks()

    assertNotNull(info)
    assertEquals(activityInfo.packageName, info!!.packageName)
    assertEquals(activityInfo.name, info.name)
  }

  @Test
  fun itShouldGetFavouriteBookmarksAsString() {
    assertEquals("package.name/ActivityName", appPreferences.getFavouriteBookmarksAsString())
  }

  @Test
  fun itShouldMapActivityInfoToString() {
    assertEquals("package.name/ActivityName", appPreferences.toString(activityInfo))
  }
}

