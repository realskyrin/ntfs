package cn.skyrin.ntfs.extentions

import androidx.compose.ui.graphics.Color

fun Color.setAlpha(alpha: Float): Color {
    return Color(
        red = this.red,
        green = this.green,
        blue = this.blue,
        alpha = alpha
    )
}