package cn.skyrin.common.constants

import android.content.ComponentName
import cn.skyrin.common.util.packageName

object Components {
    private const val componentsPackageName = "cn.skyrin.ntfh"

    val MAIN_ACTIVITY = ComponentName(packageName, "$componentsPackageName.MainActivity")
    val PROPERTIES_ACTIVITY = ComponentName(packageName, "$componentsPackageName.PropertiesActivity")
}