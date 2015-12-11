package com.stefano.playground.fastbookmark.ui.preferences

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.SmallTest
import com.stefano.playground.fastbookmark.utils.AppPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SmallTest
@RunWith(AndroidJUnit4::class)
class ViewModelTest {

  lateinit var viewModel: ViewModel

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    val packageManager = mock(PackageManager::class.java)
    `when`(packageManager.queryIntentActivities(Mockito.any(Intent::class.java),
        eq(PackageManager.MATCH_DEFAULT_ONLY)))
        .thenReturn(toResolveInfo(listOf("foo", "bar")))
    var preferences = mock(SharedPreferences::class.java)
    `when`(preferences.getString(eq("pref_list_favourite_sharing_app"), anyString()))
        .thenReturn("foo.bar/foo")
    val appPreferences = AppPreferences(preferences)
    viewModel = ViewModel(packageManager, appPreferences)
  }

  @Test
  fun itShouldHaveNonEmptyArrayOfEntries() {
    assertEquals(2, viewModel.entries!!.size)
    assertAllElementsNotNull(viewModel.entries!!)
  }

  @Test
  fun itShouldHaveNonEmptyArrayOfEntryValues() {
    assertEquals(2, viewModel.entryValues!!.size)
    assertAllElementsNotNull(viewModel.entryValues!!)
  }

  @Test
  fun itShouldHaveDefaultEntryValue() {
    assertEquals(1, viewModel.defaultEntryIndex)
  }

  private fun assertAllElementsNotNull(entries: Array<out CharSequence>) {
    for (entry in entries) {
      assertNotNull(entry)
    }
  }

  private fun toResolveInfo(activityNames: List<String>): MutableList<ResolveInfo> {
    return activityNames.map { name ->
      val resolveInfo = ResolveInfo()
      val activityInfo = ActivityInfo()
      val applicationInfo = ApplicationInfo()
      applicationInfo.packageName = "foo.bar"
      activityInfo.name = name
      resolveInfo.activityInfo = activityInfo
      resolveInfo.activityInfo.applicationInfo = applicationInfo
      resolveInfo
    } as MutableList<ResolveInfo>
  }
}