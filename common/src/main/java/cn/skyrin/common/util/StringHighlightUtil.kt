package cn.skyrin.common.util

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import java.util.regex.Pattern

class StringHighlightUtil(
    private var context: Context,
    private var wholeStr: String,
    private var highlightStr: String,
    private var color: Int,
    var ignoreCase: Boolean = false
) {
    private lateinit var spBuilder: SpannableStringBuilder

    /**
     * 填充颜色
     *
     * @return StringFormatUtil
     */
    fun fillColor(): StringHighlightUtil? {
        if (wholeStr.isNotEmpty() && highlightStr.isNotEmpty()) {

            spBuilder = SpannableStringBuilder(wholeStr)

            //匹配规则
            val p = Pattern.compile(
                highlightStr.escapeExprSpecialWord(),
                if (ignoreCase) Pattern.CASE_INSENSITIVE else 0
            )
            //匹配字段
            val m = p.matcher(spBuilder)
            //上色
            color = context.resources.getColor(color, context.theme)

            //开始循环查找里面是否包含关键字
            while (m.find()) {
                spBuilder.setSpan(
                    ForegroundColorSpan(color),
                    m.start(),
                    m.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spBuilder.setSpan(
                    BackgroundColorSpan(Color.WHITE),
                    m.start(),
                    m.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return this
        }
        return null
    }

    /**
     * 获取到已经更改好的结果(这个时候已经实现了高亮,在获取这个result的时候不要toString()要不然会把色调去除的)
     *
     * @return result
     */
    fun getResult(): SpannableStringBuilder {
        return spBuilder
    }
}