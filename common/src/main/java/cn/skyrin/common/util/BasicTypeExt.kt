package cn.skyrin.common.util

import android.content.res.Resources
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.security.MessageDigest
import java.util.*

// 快递短信签名
val QJM_SMS_SIGN = arrayOf(
    "【和驿】",
    "【来取】",
    "【社区人】",
    "【速递易】",
    "【如风达】",
    "【快递超市】",
    "【京东物流】",
    "【菜鸟驿站】",
    "【菜鸟裹裹】",
    "【顺丰速运】",
    "【百世快递】"
)

// ====================================== String ======================================
@RequiresApi(Build.VERSION_CODES.N)
fun String.isQjmSms(): Boolean {
    return Arrays.stream(QJM_SMS_SIGN).anyMatch { char: CharSequence -> this.contains(char) }
}

fun String.toUri() = Uri.parse(this)!!

fun <T> String.toObj(): T {
    val objType = object : TypeToken<T>() {}.type
    return Gson().fromJson<T>(this, objType)
}

fun String.escapeExprSpecialWord(): String {
    if (this.isBlank()) return this
    val fbsArr = arrayOf("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|")
    var rst = this
    for (key in fbsArr) {
        if (rst.contains(key)) {
            rst = rst.replace(key, "\\" + key)
        }
    }
    return rst
}

fun String.md5(): String {
    return SaSecureUtil.md5(this)
}

// ====================================== Int ======================================
val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

// ====================================== Float ======================================
val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

// ====================================== Float ======================================
fun Long.toFriendlyDate(): String {
    return getAutoTime(this)
}

// ====================================== Number ======================================
val Number.dp: Int get() = (toInt() * Resources.getSystem().displayMetrics.density).toInt()

fun Boolean?.orFalse() = this ?: false
fun Boolean?.orTrue() = this ?: true

/**
 * Returns a CharSequence that concatenates the specified array of CharSequence
 * objects and then applies a list of zero or more tags to the entire range.
 *
 * @param content an array of character sequences to apply a style to
 * @param tags the styled span objects to apply to the content
 *        such as android.text.style.StyleSpan
 */
private fun apply(content: Array<out CharSequence>, vararg tags: Any): CharSequence {
    return SpannableStringBuilder().apply {
        openTags(tags)
        content.forEach { charSequence ->
            append(charSequence)
        }
        closeTags(tags)
    }
}

/**
 * Iterates over an array of tags and applies them to the beginning of the specified
 * Spannable object so that future text appended to the text will have the styling
 * applied to it. Do not call this method directly.
 */
private fun Spannable.openTags(tags: Array<out Any>) {
    tags.forEach { tag ->
        setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK)
    }
}

/**
 * "Closes" the specified tags on a Spannable by updating the spans to be
 * endpoint-exclusive so that future text appended to the end will not take
 * on the same styling. Do not call this method directly.
 */
private fun Spannable.closeTags(tags: Array<out Any>) {
    tags.forEach { tag ->
        if (length > 0) {
            setSpan(tag, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            removeSpan(tag)
        }
    }
}

/**
 * Returns a CharSequence that applies boldface to the concatenation
 * of the specified CharSequence objects.
 */
fun bold(vararg content: CharSequence): CharSequence = apply(content, StyleSpan(
    Typeface.BOLD))

/**
 * Returns a CharSequence that applies italics to the concatenation
 * of the specified CharSequence objects.
 */
fun italic(vararg content: CharSequence): CharSequence = apply(content, StyleSpan(Typeface.ITALIC))

/**
 * Returns a CharSequence that applies a foreground color to the
 * concatenation of the specified CharSequence objects.
 */
fun color(color: Int, vararg content: CharSequence): CharSequence =
    apply(content, ForegroundColorSpan(color))