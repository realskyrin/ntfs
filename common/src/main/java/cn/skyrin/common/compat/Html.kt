@file:Suppress("DEPRECATION")

package cn.skyrin.common.compat

import android.os.Build
import android.text.Html
import android.text.Spanned

fun fromHtmlCompat(content: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(content)
    }
}