package cn.skyrin.ntfs.util

import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocaleUtils {
    // [AppPrefs] is sharedpreferences or datastore
    fun setLocale(c: Context, languageZh: Boolean) = updateResources(c, if (languageZh) "zh" else "en") //use locale codes

    private fun updateResources(context: Context, language: String) {
        context.resources.apply {
            val locale = Locale(language)
            val config = Configuration(configuration)

            context.createConfigurationContext(configuration)
            Locale.setDefault(locale)
            config.setLocale(locale)
            context.resources.updateConfiguration(config, displayMetrics)
        }
    }
}
