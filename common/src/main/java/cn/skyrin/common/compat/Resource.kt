@file:Suppress("DEPRECATION")

package cn.skyrin.common.compat

import android.content.res.Configuration
import android.os.Build
import java.util.*

val Configuration.preferredLocale: Locale
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locales[0]
        } else {
            locale
        }
    }