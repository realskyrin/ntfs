package cn.skyrin.ntfs.util

import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocaleUtils {
    fun setLocale(c: Context, languageZh: Boolean) = updateResources(c, if (languageZh) "zh" else "en")

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
