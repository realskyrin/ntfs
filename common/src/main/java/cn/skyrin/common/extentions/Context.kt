package cn.skyrin.common.extentions

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import cn.skyrin.common.R
import cn.skyrin.common.util.ClipboardHelper

fun Context.copyText2Clipboard(
    text: String?,
    label: String = "copied text",
    clear: Boolean = false, // clear before copy
    copyError: ((e: Exception) -> Unit)? = null,
    copySuccess: (() -> Unit)? = null,
) {
    ClipboardHelper.copyText2Clipboard(this, text, label, clear, copyError, copySuccess)
}

fun Context.getTextFromClipboard(clear: Boolean = false): String {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val text = clipboard.primaryClip?.getItemAt(0)?.text
    if (clear) clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
    return text.toString()
}

fun Context.shareText2OtherApp(title: String, text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, title)
    startActivity(shareIntent)
}

fun Context.startActivitySafe(intent: Intent, options: Bundle? = null) {
    try {
        startActivity(intent, options)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show()
    }
}

fun Context.openUrlOnBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivitySafe(intent)
}
