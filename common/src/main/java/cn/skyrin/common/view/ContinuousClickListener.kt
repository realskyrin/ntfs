package cn.skyrin.common.view

import android.os.SystemClock
import android.view.View

const val COUNTS = 999 // 点击次数
const val DURATION: Long = 1000 // 每次点击有效最大间隔时间

abstract class OnContinuousClickListener(
    private val count: Int = COUNTS,
    private val time: Long = DURATION,
) : View.OnClickListener {
    var mHits = LongArray(COUNTS)

    init {
        mHits = LongArray(count)
    }

    override fun onClick(v: View) {
        // 每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
        // 为数组最后一位赋值
        mHits[mHits.size - 1] = SystemClock.uptimeMillis()
        if (mHits[0] >= (SystemClock.uptimeMillis() - time)) {
            // 重新初始化数组
            mHits = LongArray(count)
            // exec action
            onContinuousClick(v)
        }
        onVClick(v)
    }

    abstract fun onContinuousClick(v: View)
    abstract fun onVClick(v: View)
}
