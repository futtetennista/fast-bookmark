package com.stefano.playground.fastbookmark.background.service

class FastBookmarkerTest {

  @Mock lateinit var clipboardManager: ClipboardManager
  @Mock lateinit var applicationContext: Context
  @Mock lateinit var packageManagerDelegate: PackageManagerDelegate
  @Mock lateinit var preferences: AppPreferences
  @Mock lateinit var intentFactory: IntentFactory
  @Mock lateinit var primaryClip: ClipData
  @Mock lateinit var item: ClipData.Item
  @Mock lateinit var chooserIntent: Intent
  @Mock lateinit var favouriteBookmarksAppIntent: Intent
  @Mock lateinit var shareIntent: Intent
  @Mock lateinit var appIntent: Intent

  lateinit var fastBookmarker: FastBookmarker

  @Before
  fun before() {
    MockitoAnnotations.initMocks(this)
    setupMocks()

    fastBookmarker =
        FastBookmarker(clipboardManager, applicationContext, packageManagerDelegate,
            preferences, intentFactory)
  }

  private fun setupMocks() {
    `when`(primaryClip.getItemAt(0))
        .thenReturn(item)
    `when`(intentFactory.createChooserIntent(any(ClipData.Item::class.java)))
        .thenReturn(chooserIntent)
    `when`(intentFactory.createFavouriteBookmarksAppIntent(any(ClipData.Item::class.java), any()))
        .thenReturn(favouriteBookmarksAppIntent)
    `when`(intentFactory.createShareIntent(any(ClipData.Item::class.java)))
        .thenReturn(shareIntent)
    `when`(intentFactory.createAppIntent())
        .thenReturn(appIntent)
  }

  @Test
  fun it_should_add_itself_as_clipboard_listener_on_activate() {
    fastBookmarker.activate()

    verify(clipboardManager).addPrimaryClipChangedListener(fastBookmarker)
  }

  @Test
  fun it_should_remove_itself_as_clipboard_listener_on_deactivate() {
    fastBookmarker.deactivate()

    verify(clipboardManager).removePrimaryClipChangedListener(fastBookmarker)
  }

  @Test
  fun it_should_do_nothing_if_clipboard_is_empty() {
    fastBookmarker.onPrimaryClipChanged()

    verifyZeroInteractions(applicationContext)
  }

  @Test
  fun it_should_do_nothing_if_clipboard_item_is_not_a_link() {
    mockClipboard("I am not a link")

    fastBookmarker.onPrimaryClipChanged()

    verifyZeroInteractions(applicationContext)
  }

  @Test
  fun it_should_send_link_to_favourite_read_later_app_if_clipboard_item_is_a_link() {
    mockClipboard("https://kotlinlang.org")
    `when`(preferences.getFavouriteBookmarks())
      .thenReturn(mock(ActivityInfo::class.java))
    `when`(packageManagerDelegate.isIntentHandlerInstalled(
        eq(shareIntent), any(ActivityInfo::class.java)))
        .thenReturn(true)

    fastBookmarker.onPrimaryClipChanged()

    verify(applicationContext).startActivity(favouriteBookmarksAppIntent)
  }

  @Test
  fun it_should_start_preferences_screen_if_clipboard_item_is_a_link_but_no_favourite_read_later_app_chosen() {
    mockClipboard("https://kotlinlang.org")
    `when`(preferences.getFavouriteBookmarks())
        .thenReturn(null)
    `when`(packageManagerDelegate.canAnyInstalledAppHandleIntent(chooserIntent))
        .thenReturn(true)

    fastBookmarker.onPrimaryClipChanged()

    verify(applicationContext).startActivity(chooserIntent)
  }

  @Test
  fun it_should_start_preferences_screen_if_clipboard_item_is_a_link_but_chosen_favourite_read_later_app_uninstalled() {
    mockClipboard("https://kotlinlang.org")
    `when`(preferences.getFavouriteBookmarks())
        .thenReturn(null)
    `when`(packageManagerDelegate.canAnyInstalledAppHandleIntent(chooserIntent))
        .thenReturn(false)
    fastBookmarker.isTestRun = true

    fastBookmarker.onPrimaryClipChanged()

    verify(applicationContext).startActivity(appIntent)
  }

  private fun mockClipboard(text: String) {
    `when`(clipboardManager.hasPrimaryClip())
        .thenReturn(true)
    `when`(clipboardManager.primaryClip)
        .thenReturn(primaryClip)
    `when`(item.text)
        .thenReturn(text)
  }
}
