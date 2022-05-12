@file:Suppress("DEPRECATION")

package cn.skyrin.common.compat

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import org.intellij.lang.annotations.Language
import java.util.*


//fun Context.getColorCompat(@ColorRes id: Int): Int {
//    return if (atLeastM()) {
//        this.getColor(id)
//    } else {
//        resources.getColor(id)
//    }
//}

@ColorInt
fun Context.getColorCompat(@ColorRes id: Int): Int = getColorStateListCompat(id).defaultColor

fun Context.getColorStateListCompat(@ColorRes id: Int): ColorStateList =
    AppCompatResources.getColorStateList(this, id)!!

fun Context.getColorFromAttr(@AttrRes id: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return AppCompatResources.getDrawable(this, id)
}

fun Context.getDrawableFromPkgName(pkg: String): Drawable? {
    return try {
        packageManager.getApplicationIcon(pkg)
    } catch (e: Exception) {
        null
    }
}

fun Context.isNotificationAccessEnabled(): Boolean {
    val enabledAppList: Set<String>
    try {
        enabledAppList = NotificationManagerCompat.getEnabledListenerPackages(this.applicationContext)
    } catch (e: Exception) {
        return false
    }
    return enabledAppList.contains(packageName)
}

fun Context.openNotificationServiceSettings() {
    try {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openAppDetailPage(pkgName: String) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.data = Uri.parse("package:$pkgName")
    (this as? Activity)?.startActivity(intent)
}

fun Context.openApp(pkgName: String, openFail: () -> Unit) {
    val intent = packageManager.getLaunchIntentForPackage(pkgName)
    try {
        startActivity(intent)
    } catch (e: Exception) {
        openFail()
    }
}

@SuppressLint("ObsoleteSdkInt")
fun Context.openNotificationSettings(pkgName: String, openFail: () -> Unit) {
    val intent = Intent()
    val appInfo = try {
        packageManager.getApplicationInfo(pkgName, 0)
    } catch (e: Exception) {
        null
    }
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkgName)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", pkgName)
            intent.putExtra("app_uid", appInfo?.uid)
        }
        else -> {
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:$pkgName")
        }
    }
    try {
        startActivity(intent)
    } catch (e: Exception) {
        openFail()
    }
}

fun Context.getAppName(pkgName: String): String {
    val appInfo = try {
        packageManager.getApplicationInfo(pkgName, 0)
    } catch (e: Exception) {
        null
    }
    return appInfo?.loadLabel(packageManager).toString()
}

fun Fragment.getAppName(pkgName: String): String {
    return context?.getAppName(pkgName).toString()
}

/**
 *  {
 *     "icon": 2131558400,
 *     "label": "系统语音引擎",
 *     "name": "com.xiaomi.mibrain.speech"
 *  }
 */
fun Context.getTtsEnginesLabel(): List<String> {
    return try {
        val tts = TextToSpeech(this.applicationContext, null)
        tts.engines.map { it.label }
    } catch (e: Exception) {
        emptyList()
    }
}

/**
 *  {
 *     "icon": 2131558400,
 *     "label": "系统语音引擎",
 *     "name": "com.xiaomi.mibrain.speech"
 *  }
 */
fun Context.getTtsEnginesName(): List<String> {
    return try {
        val tts = TextToSpeech(this.applicationContext, null)
        tts.engines.map { it.name }
    } catch (e: Exception) {
        emptyList()
    }
}

/**
 *  {
 *     "icon": 2131558400,
 *     "label": "系统语音引擎",
 *     "name": "com.xiaomi.mibrain.speech"
 *  }
 */
fun Context.getTtsDefEnginesName(): String? {
    return try {
        val tts = TextToSpeech(this.applicationContext, null)
        return tts.defaultEngine
    } catch (e: Exception) {
        null
    }
}

/**
 *  {
 *     "icon": 2131558400,
 *     "label": "系统语音引擎",
 *     "name": "com.xiaomi.mibrain.speech"
 *  }
 */
fun Context.getTtsDefEnginesLabel(): String? {
    return getTtsLabelByName(getTtsDefEnginesName())
}

fun Context.getTtsLabelByName(ttsName: String?): String? {
    return try {
        val tts = TextToSpeech(this.applicationContext, null)
        tts.engines.forEach {
            if (it.name == ttsName) return it.label
        }
        return null
    } catch (e: Exception) {
        null
    }
}

fun Context.getTtsVoices(): List<Voice> {
    return try {
        val tts = TextToSpeech(this.applicationContext, null)
        return tts.voices.toList()
    } catch (e: Exception) {
        emptyList()
    }
}

fun Context.getTtsDefVoice(): Voice? {
    return try {
        val tts = TextToSpeech(this.applicationContext, null)
        return tts.defaultVoice
    } catch (e: Exception) {
        null
    }
}

fun Context.getTtsVoice(): Voice? {
    return try {
        val tts = TextToSpeech(this.applicationContext, null)
        return tts.voice
    } catch (e: Exception) {
        null
    }
}

fun Context.getTtsLanguages(): List<Locale> {
    return try {
        val tts = TextToSpeech(this.applicationContext, null)
        return tts.availableLanguages.toList()
    } catch (e: Exception) {
        emptyList()
    }
}

fun Context.getTtsDefLanguages(): Locale? {
    return try {
        val tts = TextToSpeech(this.applicationContext, null)
        return tts.language
    } catch (e: Exception) {
        null
    }
}

