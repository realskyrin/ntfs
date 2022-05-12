package cn.skyrin.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.SwitchCompat
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import cn.skyrin.common.util.AnimatorHelper
import cn.skyrin.common.util.Spanny
import cn.skyrin.common.view.ViewClickDelay.SPACE_TIME
import cn.skyrin.common.view.ViewClickDelay.hash
import cn.skyrin.common.view.ViewClickDelay.lastClickTime
import com.google.android.material.chip.Chip
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import kotlin.math.absoluteValue

fun View.gone() {
    if (this.isGone) return
    this.visibility = View.GONE
}

fun View.invisible() {
    if (this.isInvisible) return
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    if (this.isVisible) return
    this.visibility = View.VISIBLE
}

fun View.showOrHide(show: Boolean) {
    if (show) this.visible() else this.invisible()
}

fun View.showOrGone(show: Boolean) {
    if (show) this.visible() else this.gone()
}

//fun View.showOrGoneWithAnimator(show: Boolean, duration: Long = 300L) {
//    if (show) this.visibleWithAnimator(duration) else this.goneWithAnimator(duration)
//}

fun View.visibleWithExpandAnimation(duration: Long = 300L) {
    getExpandAnimation(duration).start()
}

fun View.getCollapseAnimation(duration: Long = 300L): Animation {
    val anim = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    anim.duration = duration
    return anim
}

fun View.getExpandAnimation(duration: Long = 300L): Animation {
    val anim = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    anim.duration = duration
    return anim
}

fun View.showOrGoneWithAnimBottom(show: Boolean) {
    if (isVisible == show) return
    val animIn = AnimatorHelper.createBottomInAnim(this)
    val animOut = AnimatorHelper.createBottomOutAnim(this)
    animIn.doOnStart { visible() }
    animOut.doOnEnd { gone() }
    if (show) animIn.start() else animOut.start()
}

fun View.showOrGoneWithAnimTop(show: Boolean) {
    if (isVisible == show) return
    val animIn = AnimatorHelper.createTopInAnim(this)
    val animOut = AnimatorHelper.createTopOutAnim(this)
    animIn.doOnStart { visible() }
    animOut.doOnEnd { gone() }
    if (show) animIn.start() else animOut.start()
}

fun View.showOrGoneWithAnimL2R(show: Boolean) {
    if (isVisible == show) return
    val animIn = AnimatorHelper.createLeftInAnim(this)
    val animOut = AnimatorHelper.createRightOutAnim(this)
    animIn.doOnStart { visible() }
    animOut.doOnEnd { gone() }
    if (show) animIn.start() else animOut.start()
}

fun View.showOrGoneWithAnimR2L(show: Boolean) {
    if (isVisible == show) return
    val animIn = AnimatorHelper.createRightInAnim(this)
    val animOut = AnimatorHelper.createLeftOutAnim(this)
    animIn.doOnStart { visible() }
    animOut.doOnEnd { gone() }
    if (show) animIn.start() else animOut.start()
}

fun View.showOrGoneWithAnimRi2Ro(show: Boolean) {
    if (isVisible == show) return
    val animIn = AnimatorHelper.createRightInAnim(this)
    val animOut = AnimatorHelper.createRightOutAnim(this)
    animIn.doOnStart { visible() }
    animOut.doOnEnd { gone() }
    if (show) animIn.start() else animOut.start()
}

fun View.showOrGoneWithAnimLi2Lo(show: Boolean) {
    if (isVisible == show) return
    val animIn = AnimatorHelper.createLeftInAnim(this)
    val animOut = AnimatorHelper.createLeftOutAnim(this)
    animIn.doOnStart { visible() }
    animOut.doOnEnd { gone() }
    if (show) animIn.start() else animOut.start()
}

fun View.toggleVisible() {
    if (isVisible) gone() else visible()
}

fun View.toggleRotation(rotation: Float) {
    if (this.rotation == rotation) setRotation(0f) else setRotation(rotation)
}

fun View.margins(size: Int) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.setMargins(size)
}

fun View.marginTop(size: Int) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = size
}

fun View.marginRight(size: Int) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin = size
}

fun View.marginBottom(size: Int) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = size
}

fun View.marginLeft(size: Int) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin = size
}

fun View.marginHorizontal(size: Int) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.setMargins(size, 0, size, 0)
}

var View.paddingStartCompat: Int
    set(value) {
        setPadding(value, paddingTop, paddingEnd, paddingBottom)
    }
    get() = paddingStart

fun View.addPaddingStart(padding: Int) {
    setPadding(paddingStart + padding, paddingTop, paddingEnd, paddingBottom)
}

fun View.addPaddingHorizontal(padding: Int) {
    setPadding(paddingStart + padding, paddingTop, paddingEnd + padding, paddingBottom)
}

var View.paddingTopCompat: Int
    set(value) {
        setPadding(paddingStart, value, paddingEnd, paddingBottom)
    }
    get() = paddingTop

fun View.addPaddingTop(padding: Int) {
    setPadding(paddingStart, paddingTop + padding, paddingEnd, paddingBottom)
}

var View.paddingEndCompat: Int
    set(value) {
        setPadding(paddingStart, paddingTop, value, paddingBottom)
    }
    get() = paddingEnd

fun View.addPaddingEnd(padding: Int) {
    setPadding(paddingStart, paddingTop, paddingEnd + padding, paddingBottom)
}

var View.paddingBottomCompat: Int
    set(value) {
        setPadding(paddingStart, paddingTop, paddingEnd, value)
    }
    get() = paddingBottom

fun View.addPaddingBottom(padding: Int) {
    setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom + padding)
}

/**
 * 请求输入焦点
 */
fun View.requestTextInput() {
    post {
        requestFocus()

        postDelayed({
            context.getSystemService<InputMethodManager>()
                ?.showSoftInput(this, 0)
        }, 300)
    }
}

@SuppressLint("ClickableViewAccessibility")
fun View.addClickScale(scale: Float = 0.95f, duration: Long = 150) {
    this.setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.animate().scaleX(scale).scaleY(scale).setDuration(duration).start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                this.animate().scaleX(1f).scaleY(1f).setDuration(duration).start()
            }
        }
        this.onTouchEvent(event)
    }
}

@SuppressLint("ClickableViewAccessibility")
fun View.addClickShake(
    fromX: Float = 0.0f,
    toX: Float = 1.0f,
    fromY: Float = 0.0f,
    toY: Float = 1.0f,
    duration: Long = 35,
    repeatCount: Int = 3,
) {
    this.setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                this.shake(fromX, toX, fromY, toY, duration, repeatCount)
            }
        }
        this.onTouchEvent(event)
    }
}

fun View.scaleBlink(
    fromX: Float = 0.0f,
    toX: Float = 1.0f,
    fromY: Float = 0.0f,
    toY: Float = 1.0f,
    duration: Long = 150,
    repeatCount: Int = 3,
) {
    if (repeatCount == 0) return
    this.animate().scaleX(fromX).scaleY(fromY).setDuration(duration).withEndAction {
        this.animate().scaleX(toX).scaleY(toY).setDuration(duration).withEndAction {
            this.scaleBlink(fromX, toX, fromY, toY, duration, repeatCount - 1)
        }.start()
    }.start()
}

fun View.blink(
    fromAlpha: Float = 0.0f,
    toAlpha: Float = 1.0f,
    duration: Long = 390,
    repeatCount: Int = 3,
) {
    val anim = AlphaAnimation(fromAlpha, toAlpha)
    anim.duration = duration
    anim.startOffset = 20
    anim.repeatMode = Animation.REVERSE
    anim.repeatCount = repeatCount
    this.startAnimation(anim)
}

fun View.shake(
    fromX: Float = 0.0f,
    toX: Float = 1.0f,
    fromY: Float = 0.0f,
    toY: Float = 1.0f,
    duration: Long = 100,
    repeatCount: Int = 3,
) {
    val anim = TranslateAnimation(fromX, toX, fromY, toY)
    anim.duration = duration
    anim.startOffset = 20
    anim.repeatMode = Animation.REVERSE
    anim.repeatCount = repeatCount
    this.startAnimation(anim)
}

fun View.onContinuousClick(
    count: Int = COUNTS,
    time: Long = DURATION,
    click: ((view: View) -> Unit)? = null,
    continuousClick: (view: View) -> Unit,
) {
    setOnClickListener(object : OnContinuousClickListener(count, time) {
        override fun onContinuousClick(v: View) = continuousClick(v)
        override fun onVClick(v: View) {
            click?.invoke(v)
        }
    })
}

object ViewClickDelay {
    var hash: Int = 0
    var lastClickTime: Long = 0
    var SPACE_TIME: Long = 2000  // 间隔时间
}

/**
 * 防快速点击
 * @receiver View
 * @param clickAction 要响应的操作
 */
infix fun View.clickDelay(clickAction: () -> Unit) {
    this.setOnClickListener {
        if (this.hashCode() != hash) {
            hash = this.hashCode()
            lastClickTime = System.currentTimeMillis()
            clickAction()
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > SPACE_TIME) {
                lastClickTime = System.currentTimeMillis()
                clickAction()
            }
        }
    }
}

fun View.removeSelf() {
    (parent as? ViewGroup)?.removeView(this)
}

/**
 * Sets a click listener that prevents quick repeated clicks.
 *
 * @author Aidan Follestad (@afollestad)
 */
fun View.onDebouncedClick(
    delayBetweenClicks: Long = DEFAULT_DEBOUNCE_INTERVAL,
    click: (view: View) -> Unit,
) {
    setOnClickListener(object : DebouncedOnClickListener(delayBetweenClicks) {
        override fun onDebouncedClick(v: View) = click(v)
    })
}

var View.contentDescriptionSuffix: CharSequence
    get() = throw UnsupportedOperationException("set value only")
    set(suffix) {
        contentDescription =
            if (contentDescription.isNullOrEmpty()) "$suffix" else "$contentDescription, $suffix"
    }

fun Chip.getCloseChip(text: String): Chip {
    this.text = text
    isCheckedIconVisible = false
    isCloseIconVisible = true
    return this
}

fun Chip.getAppChip(text: String, icon: Drawable? = null): Chip {
    this.text = text
    isCheckedIconVisible = false
    isCloseIconVisible = true
    chipIcon = icon
    return this
}

fun SwitchCompat.turnOff() {
    if (!isChecked) return
    isChecked = false
}

fun SwitchCompat.turnOn() {
    if (isChecked) return
    isChecked = true
}

fun TextView.addSpan(
    spannyText: String = text.toString(),
    textToSpan: String? = null,
    emphasisColor: Int? = null,
    ignoreCase: Boolean = false
) {
    val spanny = Spanny(spannyText)
    spanny.findAndSpan(textToSpan ?: spannyText, object : Spanny.GetSpan {
        override val span: Any
            get() = ForegroundColorSpan(emphasisColor ?: currentTextColor)
    }, ignoreCase)

    text = spanny
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
}

fun TextView.addClickableSpan(
    spannyText: String = text.toString(),
    textToSpan: String? = null,
    emphasisColor: Int? = null,
    onClick: (widget: View) -> Unit
) {
    val spanny = Spanny(spannyText)
    spanny.findAndSpan(textToSpan ?: spannyText, object : Spanny.GetSpan {
        override val span: Any
            get() = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onClick(widget)
                }
            }
    }).findAndSpan(textToSpan ?: spannyText, object : Spanny.GetSpan {
        override val span: Any
            get() = ForegroundColorSpan(emphasisColor ?: currentTextColor)
    })

    text = spanny
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
}

fun TextView.addClickableSpanWithIcon(
    spannyText: String = text.toString(),
    textToSpan: String? = null,
    emphasisColor: Int? = null,
    iconName: String,
    @DrawableRes
    iconSrc: Int,
    onClick: (widget: View) -> Unit
) {
    val spanny = Spanny(spannyText)
    spanny.findAndSpan(textToSpan ?: spannyText, object : Spanny.GetSpan {
        override val span: Any
            get() = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onClick(widget)
                }
            }
    }).findAndSpan(textToSpan ?: spannyText, object : Spanny.GetSpan {
        override val span: Any
            get() = ForegroundColorSpan(emphasisColor ?: currentTextColor)
    }).findAndSpan(iconName, object : Spanny.GetSpan {
        override val span: Any
            get() = ImageSpan(this@addClickableSpanWithIcon.context, iconSrc)
    })

    text = spanny
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
}

/**
 * 同时 span 多个 text 和 icon
 * @param textToSpan    Map<text, onClick{}>
 * @param iconToSpan    Map<iconName, iconSrc>
 */
fun TextView.addClickableSpansWithIcons(
    spannyText: String = text.toString(),
    textToSpan: MutableMap<String, (widget: View) -> Unit> = mutableMapOf(),
    emphasisColor: Int? = null,
    iconToSpan: MutableMap<String, Int> = mutableMapOf(),
) {
    val spanny = Spanny(spannyText)
    textToSpan.forEach {
        spanny.findAndSpan(it.key, object : Spanny.GetSpan {
            override val span: Any
                get() = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        it.value(widget)
                    }
                }
        }).findAndSpan(it.key, object : Spanny.GetSpan {
            override val span: Any
                get() = ForegroundColorSpan(emphasisColor ?: Color.BLUE)
        })
    }

    iconToSpan.forEach {
        spanny.findAndSpan(it.key, object : Spanny.GetSpan {
            override val span: Any
                get() = ImageSpan(this@addClickableSpansWithIcons.context, it.value)
        })
    }

    text = spanny
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
}

fun makeSpannableString(
    spannyText: String,
    textToSpan: MutableMap<String, (widget: View) -> Unit> = mutableMapOf(),
    emphasisColor: Int? = null,
    ctx: Context? = null,
    iconToSpan: MutableMap<String, Int> = mutableMapOf(),
): Spanny {
    val spanny = Spanny(spannyText)
    textToSpan.forEach {
        spanny.findAndSpan(it.key, object : Spanny.GetSpan {
            override val span: Any
                get() = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        it.value(widget)
                    }
                }
        }).findAndSpan(it.key, object : Spanny.GetSpan {
            override val span: Any
                get() = ForegroundColorSpan(emphasisColor ?: Color.BLUE)
        })
    }

    ctx?.let {
        iconToSpan.forEach {
            spanny.findAndSpan(it.key, object : Spanny.GetSpan {
                override val span: Any
                    get() = ImageSpan(ctx, it.value)
            })
        }
    }

    return spanny
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.setup() {
    settings.apply {
        javaScriptEnabled = true
        layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        displayZoomControls = false
        builtInZoomControls = true
        domStorageEnabled = true
        setSupportZoom(true)
    }
}

/**
 * RecyclerView 常用的滚动监听
 * [onTop] 到顶部时触发
 * [onBottom] 到底部时触发
 * [onIdle] 滚动暂停时触发
 * [onDragging] 拖拽时触发
 * [onSettling] 惯性滚动时触发
 * [scroll] 滚动过的距离并回传
 */
fun RecyclerView.onScroll(
    onTop: (() -> Unit)? = null,
    onBottom: (() -> Unit)? = null,
    onIdle: (() -> Unit)? = null,
    onDragging: (() -> Unit)? = null,
    onSettling: (() -> Unit)? = null,
    scroll: (Int) -> Unit
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                // 暂停
                SCROLL_STATE_IDLE -> onIdle?.invoke()
                // 拖动
                SCROLL_STATE_DRAGGING -> onDragging?.invoke()
                // 惯性滑动
                SCROLL_STATE_SETTLING -> onSettling?.invoke()
            }
        }

        override fun onScrolled(
            recyclerView: RecyclerView,
            dx: Int,
            dy: Int,
        ) {
            super.onScrolled(recyclerView, dx, dy)
            scroll(computeVerticalScrollOffset())

            if (recyclerView.canScrollVertically(-1).not()) {
                // 滑动到顶部
                onTop?.invoke()
            }
            if (recyclerView.canScrollVertically(1).not()) {
                // 滑动到底部
                onBottom?.invoke()
            }
        }
    })
}

/**
 * 跳转到顶部，无动画
 */
fun RecyclerView.jump2Top() {
    if (this.adapter?.itemCount ?: 0 > 0) {
        jump2Position(position = 0)
    }
}

/**
 * 滚动到顶部
 */
fun RecyclerView.smoothScroll2Top() {
    if (this.adapter?.itemCount ?: 0 > 0) {
        this.smoothScrollToPosition(0)
    }
}


/**
 * 跳转到 [position]，无动画
 * [onScrolled] 滚动过程回调 dx dy 分别为水平和垂直方向的滚动距离
 * [onScrollDone] 滚动结束回调
 */
fun RecyclerView.jump2Position(
    position: Int,
    onScrolled: ((dx: Int, dy: Int) -> Unit)? = null,
    onScrollDone: ((listener: OnScrollListener) -> Unit)? = null
) {
    layoutManager?.scrollToPosition(position)
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            onScrolled?.invoke(dx, dy)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == SCROLL_STATE_IDLE) {
                onScrollDone?.invoke(this)
            }
        }
    }
    addOnScrollListener(listener)
}

/**
 * 跳转到顶部并以滚动方式结束
 *
 * 用于数据量较多的情况下，在短时间内滚动到指定位置，原理是先跳转再滚动
 */
fun RecyclerView.smoothJump2Top() {
    if (this.adapter?.itemCount ?: 0 > 0) {
        smoothJump2Position(0)
    }
}

/**
 * 跳转到 [position] 并以滚动方式结束
 *
 * 用于数据量较多的情况下，在短时间内滚动到指定位置，原理是先跳转再滚动
 */
fun RecyclerView.smoothJump2Position(position: Int) {
    val itemCount = this.adapter?.itemCount ?: 0

    if (position > itemCount) {
        return
    }

    if (itemCount < 20 || (currentPosition() - position).absoluteValue < 20) {
        smoothScrollToPosition(position)
        return
    }

    if (position > currentPosition()) {
        scrollToPosition(position - 19)
    } else {
        scrollToPosition(position + 19)
    }

    smoothScrollToPosition(position)
}

/**
 * 跳转到 [position] 并以滚动方式结束
 *
 * 用于数据量较多的情况下，在短时间内滚动到指定位置，原理是先跳转再滚动
 * [onScrolled] 滚动过程回调 dx dy 分别为水平和垂直方向的滚动距离
 * [onScrollDone] 滚动结束回调
 */
fun RecyclerView.smoothJump2PositionWithListener(
    position: Int,
    onScrolled: ((dx: Int, dy: Int) -> Unit)? = null,
    onScrollDone: ((listener: OnScrollListener?) -> Unit)? = null
) {
    val itemCount = this.adapter?.itemCount ?: 0

    if (position > itemCount) {
        return
    }

    if (isPositionVisible(position)) {
        onScrollDone?.invoke(null)
        return
    }

    if (itemCount < 20 || (currentPosition() - position).absoluteValue < 20) {
        smoothScroll2Position(position, onScrolled, onScrollDone)
        return
    }

    if (position > currentPosition()) {
        scrollToPosition(position - 19)
    } else {
        scrollToPosition(position + 19)
    }

    smoothScroll2Position(position, onScrolled, onScrollDone)
}

/**
 * 滚动到 [position]
 * [onScrolled] 滚动过程回调 dx dy 分别为水平和垂直方向的滚动距离
 * [onScrollDone] 滚动结束回调
 */
fun RecyclerView.smoothScroll2Position(
    position: Int,
    onScrolled: ((dx: Int, dy: Int) -> Unit)? = null,
    onScrollDone: ((listener: OnScrollListener) -> Unit)? = null
) {
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            onScrolled?.invoke(dx, dy)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == SCROLL_STATE_IDLE) {
                onScrollDone?.invoke(this)
            }
        }
    }
    addOnScrollListener(listener)
    layoutManager?.smoothScrollToPosition(this, null, position)
}

/**
 * 当前第一个可见 item 的位置
 */
fun RecyclerView.currentPosition(): Int {
    val layoutManager = this.layoutManager
    if (layoutManager is LinearLayoutManager) {
        return layoutManager.findFirstVisibleItemPosition()
    }
    return 0
}

/**
 * [position] 是否处于可见位置
 */
fun RecyclerView.isPositionVisible(position: Int): Boolean {
    val layoutManager = this.layoutManager
    if (layoutManager is LinearLayoutManager) {
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        return position in firstVisibleItemPosition..lastVisibleItemPosition
    }
    return false
}

fun Drawable.save2local(path: String, name: String = "icon.jpg"): File? {
    return try {
        this.toBitmap().save2local(path, name)
    } catch (e: Exception) {
        null
    }
}

fun Drawable.toBitmapDrawable(resources: Resources): BitmapDrawable =
    if (this is BitmapDrawable) {
        this
    } else {
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        BitmapDrawable(resources, bitmap)
    }

fun Drawable.toImageView(ctx: Context): ImageView {
    return ImageView(ctx).apply {
        setImageDrawable(this@toImageView)
    }
}

fun Bitmap.save2local(path: String, name: String = "icon.jpg"): File? {
    val fileDir = File(path)

    if (fileDir.exists().not()) {
        if (fileDir.mkdirs().not()) {
            return null
        }
    }
    val pictureFile = File(fileDir.path + File.separator + name)

    kotlin.runCatching {
        val fos = FileOutputStream(pictureFile)
        this.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        fos.close()
    }

    return pictureFile
}

/**
 * 调整颜色亮度
 * 大于 1 调亮
 * 小于 1 调暗
 */
@ColorInt
fun Int.lighten(percent: Float): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    hsv[2] *= percent
    return Color.HSVToColor(hsv)
}

/**
 * 颜色转 16 进制 "#ffffff"
 */
fun Int.toHexColor(): String {
    val r = Color.red(this)
    val g = Color.green(this)
    val b = Color.blue(this)
    return String.format("#%02X%02X%02X", r, g, b)
}

/**
 * call under view.post{} so that we can get width and height
 */
fun View.toBitmap(): Bitmap? {
    return kotlin.runCatching {
        drawToBitmap()
    }.getOrNull()
}

/**
 * 将 View 做为图片存到相册
 */
fun View.saveToGallery(
    ctx: Context,
    name: String = "ntfh_image.jpg",
    done: (success: Boolean) -> Unit
) {
    val bitmap = kotlin.runCatching {
        drawToBitmap()
    }.getOrNull()
    bitmap?.save2local(ctx.filesDir.path, name)?.let {
        Timber.d("save to gallery: ${it.absolutePath}")
        kotlin.runCatching {
            MediaStore.Images.Media.insertImage(
                ctx.contentResolver,
                it.absolutePath,
                name,
                ""
            )
            done(true)
        }.getOrElse {
            done(false)
            Timber.e(it)
        }
    }
}
