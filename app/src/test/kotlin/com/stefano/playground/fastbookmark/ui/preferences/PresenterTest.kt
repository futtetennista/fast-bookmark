package com.stefano.playground.fastbookmark.ui.preferences

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.preference.ListPreference
import android.preference.Preference
import com.stefano.playground.fastbookmark.background.boot.BootBroadcastReceiver
import com.stefano.playground.fastbookmark.background.service.FastBookmarkService
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class PresenterTest {

  @Mock lateinit var context: Context
  @Mock lateinit var packageManager: PackageManager
  @Mock lateinit var factory: AndroidFactory

  private lateinit var view: SettingsView
  private lateinit var presenter: Presenter

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    mockAndroidDependencies()
    view = stubView()

    presenter = Presenter(context, packageManager, factory)
  }

  @Test
  fun itShouldEnableManifestComponents() {
    presenter.changeManifestComponentsEnableState(true)

    factory.createComponentName(context, FastBookmarkService::class.java.name)
    factory.createComponentName(context, BootBroadcastReceiver::class.java.name)
    verifyComponentEnabled(packageManager, context)
  }

  @Test
  fun itShouldDisableManifestComponents() {
    presenter.changeManifestComponentsEnableState(false)

    factory.createComponentName(context, FastBookmarkService::class.java.name)
    factory.createComponentName(context, BootBroadcastReceiver::class.java.name)
    verifyComponentDisabled(packageManager, context)
  }

  private fun mockAndroidDependencies() {
    `when`(context.startService(any(Intent::class.java)))
        .thenReturn(mock(ComponentName::class.java))
    doNothing()
        .`when`(packageManager)
        .setComponentEnabledSetting(
            any(ComponentName::class.java),
            anyInt(),
            anyInt()
        )
//    `when`(presenter.createComponentName(anyString())).thenReturn(mock(ComponentName::class.java))
  }

  private fun verifyComponentEnabled(packageManager: PackageManager,
                                     context: Context) {
    verifyComponentEnabledState(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, packageManager)
    verify(context).startService(any())
  }

  private fun verifyComponentDisabled(packageManager: PackageManager,
                                      context: Context) {
    verifyComponentEnabledState(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, packageManager)
    verify(context).stopService(any())
  }

  private fun stubView(): SettingsView {
    return object : SettingsView {
      override val sharingApp: ListPreference?
        get() = mock(ListPreference::class.java)
      override val enableAutoSharing: Preference?
        get() = mock(Preference::class.java)
    }
  }

  private fun verifyComponentEnabledState(componentEnableState: Int,
                                          packageManager: PackageManager) {
    verify(packageManager, times(2)).setComponentEnabledSetting(
        any(ComponentName::class.java),
        eq(componentEnableState),
        eq(PackageManager.DONT_KILL_APP)
    )
  }
}
