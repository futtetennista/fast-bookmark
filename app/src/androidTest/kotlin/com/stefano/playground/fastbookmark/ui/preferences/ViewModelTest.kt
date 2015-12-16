package com.stefano.playground.fastbookmark.ui.preferences

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.SmallTest
import com.stefano.playground.fastbookmark.utils.AppPreferences
import com.stefano.playground.fastbookmark.utils.AppPreferencesImpl
import com.stefano.playground.fastbookmark.utils.PackageManagerDelegate
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SmallTest
@RunWith(AndroidJUnit4::class)
class ViewModelTest {

  @Mock lateinit var packageManagerDelegate: PackageManagerDelegate
  @Mock lateinit var preferences: SharedPreferences
  lateinit var appPreferences: AppPreferences
  lateinit var viewModel: ViewModel

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    `when`(packageManagerDelegate.retrieveIntentHandlers(Mockito.any(Intent::class.java)))
        .thenReturn(toResolveInfo(mapOf(
            Pair("package.name.foo", "FooActivity"),
            Pair("package.name.bar", "BarActivity"))))
    `when`(preferences.getString(anyString(), anyString()))
        .thenReturn("package.name.foo/FooActivity")
    appPreferences = AppPreferencesImpl(preferences)
    viewModel = ViewModel(packageManagerDelegate, appPreferences)
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

  private fun assertAllElementsNotNull(entries: Array<out CharSequence?>) {
    for (entry in entries) {
      assertNotNull(entry)
    }
  }

  private fun toResolveInfo(infoMap: Map<String, String>): MutableList<ResolveInfo> {
    return infoMap.map { entry ->
      val resolveInfo = ResolveInfo()
      val activityInfo = ActivityInfo()
      val applicationInfo = ApplicationInfo()
      applicationInfo.packageName = entry.key
      activityInfo.name = entry.value
      activityInfo.packageName = entry.key
      resolveInfo.activityInfo = activityInfo
      resolveInfo.activityInfo.applicationInfo = applicationInfo
      resolveInfo
    } as MutableList<ResolveInfo>
  }
}