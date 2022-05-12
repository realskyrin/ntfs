package cn.skyrin.common.permission

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Application
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Global
import android.provider.Settings.Secure
import androidx.annotation.RequiresApi

/**
 * An abstraction layer for checking common permission access.
 *
 * @author Aidan Follestad (@afollestad)
 */
interface PermissionChecker {

  /**
   * Returns true if the app has permission to show system overlays.
   */
  fun hasOverlayPermission(): Boolean

  /**
   * Returns true if the app has permission to write external storage.
   */
  fun hasStoragePermission(): Boolean

  /**
   * Returns true if the user has enabled Developer mode.
   */
  fun hasDeveloperOptions(): Boolean
}

/** @author Aidan Follestad (@afollestad) */
class RealPermissionChecker(
  private val app: Application
) : PermissionChecker {

  @RequiresApi(Build.VERSION_CODES.M)
  override fun hasOverlayPermission(): Boolean {
    return Settings.canDrawOverlays(app)
  }

  @RequiresApi(Build.VERSION_CODES.M)
  override fun hasStoragePermission(): Boolean {
    return app.checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
  }

  @Suppress("DEPRECATION")
  @SuppressLint("ObsoleteSdkInt")
  @TargetApi(17)
  override fun hasDeveloperOptions(): Boolean {
    return when {
      Build.VERSION.SDK_INT == 16 -> Secure.getInt(
          app.contentResolver,
          Secure.DEVELOPMENT_SETTINGS_ENABLED, 0
      ) != 0
      Build.VERSION.SDK_INT >= 17 -> Secure.getInt(
          app.contentResolver,
          Global.DEVELOPMENT_SETTINGS_ENABLED, 0
      ) != 0
      else -> false
    }
  }
}
