package com.stefano.playground.fastbookmark.ui.preferences

import android.content.ComponentName
import android.content.Context

// Test-induced damage? This is useful so that I don't have to spy on the test subject
interface ComponentNameFactory {

  fun createComponentName(context: Context, className: String): ComponentName
}

class ComponentNameFactoryImpl : ComponentNameFactory {

  override fun createComponentName(context: Context, className: String): ComponentName {
    return ComponentName(context, className)
  }
}
