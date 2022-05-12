package cn.skyrin.common.util

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.AnyThread

@SuppressLint("WrongThread")
object ClipboardHelper {
    private val handler = Handler(Looper.getMainLooper())

    @AnyThread
    fun copyText2Clipboard(
        context: Context,
        text: String?,
        label: String = "copied text",
        clear: Boolean = false, // clear before copy
        copyError: ((e: Exception) -> Unit)? = null,
        copySuccess: (() -> Unit)? = null,
    ) {
        if (Looper.getMainLooper().thread === Thread.currentThread()) {
            copy(context, text, label, clear, copyError, copySuccess)
        } else {
            handler.post { copy(context, text, label, clear, copyError, copySuccess) }
        }
    }

    fun copy(
        context: Context,
        text: String?,
        label: String = "copied text",
        clear: Boolean = false, // clear before copy
        copyError: ((e: Exception) -> Unit)? = null,
        copySuccess: (() -> Unit)? = null,
    ) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clear) clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
        val clip: ClipData = ClipData.newPlainText(label, text)
        try {
            clipboard.setPrimaryClip(clip)
        } catch (e: Exception) {
            copyError?.invoke(e)
            return
        }
        copySuccess?.invoke()
    }
}
