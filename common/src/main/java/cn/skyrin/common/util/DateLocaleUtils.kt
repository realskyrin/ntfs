package cn.skyrin.common.util

import android.content.res.Resources
import cn.skyrin.common.R
import java.text.SimpleDateFormat
import java.util.*

fun getAutoTime(long: Long, res: Resources): String {
    return getAutoTime(Date(long), res)
}

fun getAutoTime(date: Date, res: Resources): String {
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
            getDateInYear(date, res)
        } else {
            getDate(date, res)
        }
    } else if (dateL < todayL - 172000000L) { // 比今天0时还早48小时以上
//            ((todayL - dateL) / 86400000L).toString() + "天前"
        getDateInYear(date, res)
    } else if (dateL > nowL - 600000L) { // 十分钟以内
        res.getString(R.string.just_now) + getTime(date)
    } else if (dateL < todayL - 86400000L) { // 比今天0时还早24小时以上
        res.getString(R.string.tow_days_ago) + getTime(date)
    } else if (dateL < todayL) { // 比今天0时还早
        res.getString(R.string.yesterday) + getTime(date)
    } else if (dateL < todayL + 21600000L) { // 今天6点前
        res.getString(R.string.early_morning) + getTime(date)
    } else if (dateL < todayL + 43200000L) { // 今天12点前
        res.getString(R.string.am) + getTime(date)
    } else if (dateL < todayL + 64000000L) { // 今天18点前
        res.getString(R.string.pm) + getTime(date)
    } else if (dateL < todayL + 86400000L) { // 明天0时前
        res.getString(R.string.evening) + getTime(date)
    } else if (dateL < todayL + 172000000L) { // 晚今天0时48小时内
        res.getString(R.string.tomorrow) + getTime(date)
    } else if (dateL < todayL + 259200000L) { // 晚今天0时72小时内
        res.getString(R.string.tow_days_later) + getTime(date)
    } else {
        getDate(date, res)
    }
}

fun getAutoDay(date: Date, res: Resources): String {
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
            getDayInYear(date, res)
        } else {
            getDay(date, res)
        }
    } else if (dateL < todayL - 172000000L) { // 比今天0时还早48小时以上
//            ((todayL - dateL) / 86400000L).toString() + "天前"
        getDayInYear(date, res)
    } else if (dateL > nowL - 600000L) {
        res.getString(R.string.just_now)
    } else if (dateL < todayL - 86400000L) {
        res.getString(R.string.tow_days_ago)
    } else if (dateL < todayL) {
        res.getString(R.string.yesterday)
    } else if (dateL < todayL + 21600000L) {
        res.getString(R.string.early_morning)
    } else if (dateL < todayL + 43200000L) {
        res.getString(R.string.am)
    } else if (dateL < todayL + 64000000L) {
        res.getString(R.string.pm)
    } else if (dateL < todayL + 86400000L) {
        res.getString(R.string.evening)
    } else if (dateL < todayL + 172000000L) {
        res.getString(R.string.tomorrow)
    } else if (dateL < todayL + 259200000L) {
        res.getString(R.string.tow_days_later)
    } else {
        getDay(date, res)
    }
}

fun getBaseDate(date: Date, res: Resources): String {
    val sdf = SimpleDateFormat(res.getString(R.string.base_date_format), Locale.getDefault())
    return sdf.format(date)
}

fun getDate(date: Date, res: Resources): String {
    val sdf = SimpleDateFormat(res.getString(R.string.date_format), Locale.getDefault())
    return sdf.format(date)
}

fun getDateInYear(date: Date, res: Resources): String {
    val sdf = SimpleDateFormat(res.getString(R.string.date_in_year_format), Locale.getDefault())
    return sdf.format(date)
}

fun getDay(date: Date, res: Resources): String {
    val sdf = SimpleDateFormat(res.getString(R.string.day_format), Locale.getDefault())
    return sdf.format(date)
}

fun getDayInYear(date: Date, res: Resources): String {
    val sdf = SimpleDateFormat(res.getString(R.string.day_in_year_format), Locale.getDefault())
    return sdf.format(date)
}

fun getWeek(value: Int, res: Resources): String {
    return when (value) {
        1 -> res.getString(R.string.sunday)
        2 -> res.getString(R.string.monday)
        3 -> res.getString(R.string.tuesday)
        4 -> res.getString(R.string.wednesday)
        5 -> res.getString(R.string.thursday)
        6 -> res.getString(R.string.friday)
        else -> res.getString(R.string.saturday)
    }
}

fun Date.toFriendlyDate(res: Resources): String {
    return getAutoTime(this, res)
}

fun Date.toDay(res: Resources): String {
    return getAutoDay(this, res)
}