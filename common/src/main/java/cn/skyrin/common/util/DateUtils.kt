package cn.skyrin.common.util

import java.text.SimpleDateFormat
import java.util.*

fun getAutoTime(long: Long): String {
    return getAutoTime(Date(long))
}

fun getAutoTime(date: Date): String {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = date
    val dateL: Long = calendar.timeInMillis

    val todayCalendar: Calendar = Calendar.getInstance() // 00:00:00:000
    todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
    todayCalendar.set(Calendar.MINUTE, 0)
    todayCalendar.set(Calendar.SECOND, 0)
    todayCalendar.set(Calendar.MILLISECOND, 0)
    val todayL: Long = todayCalendar.timeInMillis

    val nowCalendar: Calendar = Calendar.getInstance()
    val nowL = nowCalendar.timeInMillis

    return if (dateL < todayL - 1296000000L) { // 比15天前还早
        if (calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) { // 在今年
            getDateInYear(date)
        } else {
            getDate(date)
        }
    } else if (dateL < todayL - 172000000L) { // 比今天0时还早48小时以上
//            ((todayL - dateL) / 86400000L).toString() + "天前"
        getDateInYear(date)
    } else if (dateL > nowL - 600000L) { // 十分钟以内
        "刚刚 " + getTime(date)
    } else if (dateL < todayL - 86400000L) { // 比今天0时还早24小时以上
        "前天 " + getTime(date)
    } else if (dateL < todayL) { // 比今天0时还早
        "昨天 " + getTime(date)
    } else if (dateL < todayL + 21600000L) { // 今天6点前
        "凌晨 " + getTime(date)
    } else if (dateL < todayL + 43200000L) { // 今天12点前
        "早上 " + getTime(date)
    } else if (dateL < todayL + 64000000L) { // 今天18点前
        "下午 " + getTime(date)
    } else if (dateL < todayL + 86400000L) { // 明天0时前
        "晚上 " + getTime(date)
    } else if (dateL < todayL + 172000000L) { // 晚今天0时48小时内
        "明天 " + getTime(date)
    } else if (dateL < todayL + 259200000L) { // 晚今天0时72小时内
        "后天 " + getTime(date)
    } else {
        getDate(date)
    }
}

fun getAutoDay(date: Date): String {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = date
    val dateL: Long = calendar.timeInMillis

    val todayCalendar: Calendar = Calendar.getInstance() // 00:00:00:000
    todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
    todayCalendar.set(Calendar.MINUTE, 0)
    todayCalendar.set(Calendar.SECOND, 0)
    todayCalendar.set(Calendar.MILLISECOND, 0)
    val todayL: Long = todayCalendar.timeInMillis

    val nowCalendar: Calendar = Calendar.getInstance()
    val nowL = nowCalendar.timeInMillis

    return if (dateL < todayL - 1296000000L) { // 比15天前还早
        if (calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) { // 在今年
            getDayInYear(date)
        } else {
            getDay(date)
        }
    } else if (dateL < todayL - 172000000L) { // 比今天0时还早48小时以上
//            ((todayL - dateL) / 86400000L).toString() + "天前"
        getDayInYear(date)
    } else if (dateL > nowL - 600000L) { // 十分钟以内
        "刚刚"
    } else if (dateL < todayL - 86400000L) { // 比今天0时还早24小时以上
        "前天"
    } else if (dateL < todayL) { // 比今天0时还早
        "昨天"
    } else if (dateL < todayL + 21600000L) { // 今天6点前
        "凌晨"
    } else if (dateL < todayL + 43200000L) { // 今天12点前
        "早上"
    } else if (dateL < todayL + 64000000L) { // 今天18点前
        "下午"
    } else if (dateL < todayL + 86400000L) { // 明天0时前
        "晚上"
    } else if (dateL < todayL + 172000000L) { // 晚今天0时48小时内
        "明天"
    } else if (dateL < todayL + 259200000L) { // 晚今天0时72小时内
        "后天"
    } else {
        getDay(date)
    }
}

fun getBaseDate(date: Date): String {
    val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:00", Locale.getDefault())
    return sdf.format(date)
}

fun getDate(date: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(date)
}

fun getDateInYear(date: Date): String {
    val sdf = SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault())
    return sdf.format(date)
}

fun getDay(date: Date): String {
    val sdf = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
    return sdf.format(date)
}

fun getDayInYear(date: Date): String {
    val sdf = SimpleDateFormat("MM月dd日", Locale.getDefault())
    return sdf.format(date)
}

fun getTime(date: Date): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(date)
}

fun getWeek(value: Int): String {
    return when (value) {
        1 -> "周日"
        2 -> "周一"
        3 -> "周二"
        4 -> "周三"
        5 -> "周四"
        6 -> "周五"
        else -> "周六"
    }
}

//fun Date.toFriendlyDate(): String {
//    return getAutoTime(this)
//}
//
//fun Date.toDay(): String{
//    return getAutoDay(this)
//}

/**
 * this Date 是否超过当前时间 给定的天数
 * @param days 给定的天数
 */
fun Date.moreThanDays(days: Int): Boolean {
    val now = Date()
    return now.time - time > days.dayToMillisecond()
}

fun Int.minutesToMillisecond(): Long {
    return this * 60 * 1000L
}

fun Int.hourToMillisecond(): Long {
    return this * 60 * 60 * 1000L
}

fun Int.dayToMillisecond(): Long {
    return this * 60 * 60 * 1000L * 24
}
