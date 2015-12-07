package com.stefano.playground.fastbookmark.ui.preferences

import android.content.ComponentName
import android.content.Context

// Only useful for unit tests. This way I don't have to spy on the test subject
interface AndroidFactory {

  fun createComponentName(context: Context, className: String): ComponentName
}

class AndroidFactoryImpl : AndroidFactory {

  override fun createComponentName(context: Context, className: String): ComponentName {
    return ComponentName(context, className)
  }
}
