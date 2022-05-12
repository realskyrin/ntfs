@file:Suppress("DEPRECATION")

package cn.skyrin.common.compat

import android.content.pm.PackageInfo

val PackageInfo.versionCodeCompat: Long
    get() {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            longVersionCode
        } else {
            versionCode.toLong()
        }
    }
