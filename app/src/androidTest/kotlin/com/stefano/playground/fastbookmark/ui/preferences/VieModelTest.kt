package com.stefano.playground.fastbookmark.ui.preferences

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.test.MoreAsserts.assertNotEmpty
import com.stefano.playground.fastbookmark.utils.AppPreferences
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class ViewModelTest {

  @Mock lateinit var packageManager: PackageManager
  @Mock lateinit var appPreferences: AppPreferences
  lateinit var viewModel: ViewModel

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    `when`(packageManager.queryIntentActivities(Mockito.any(Intent::class.java),
        PackageManager.MATCH_DEFAULT_ONLY)).thenReturn(toResolveInfo(listOf("foo", "bar")))

    viewModel = ViewModel(packageManager, appPreferences)
  }

  @Test
  fun itShouldHaveNonEmptyArrayOfEntries() {
    assertNotEmpty(viewModel.entries!!.toArrayList())
  }

  @Test
  fun itShouldHaveNonEmptyArrayOfEntryValues() {
    assertNotEmpty(viewModel.entryValues?.toArrayList())
  }

  @Test
  fun itShouldHaveDefaultEntryValue() {
    assertEquals(1, viewModel.defaultEntryIndex)
  }

  private fun toResolveInfo(activityNames: List<String>): MutableList<ResolveInfo> {
    return activityNames.map { name ->
      val resolveInfo = ResolveInfo()
      val activityInfo = ActivityInfo()
      activityInfo.name = name
      resolveInfo.activityInfo = activityInfo
      resolveInfo
    } as MutableList<ResolveInfo>
  }
}