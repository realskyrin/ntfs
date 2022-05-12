@file:Suppress("DEPRECATION")

package cn.skyrin.common.compat

import android.content.pm.PackageManager
import OsUtils.atLeastN

object VersionCompat {
  val MATCH_DISABLED_COMPONENTS = if (atLeastN()) {
    PackageManager.MATCH_DISABLED_COMPONENTS
  } else {
    PackageManager.GET_DISABLED_COMPONENTS
  }

  val MATCH_UNINSTALLED_PACKAGES = if (atLeastN()) {
    PackageManager.MATCH_UNINSTALLED_PACKAGES
  } else {
    PackageManager.GET_UNINSTALLED_PACKAGES
  }
}
