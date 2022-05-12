package cn.skyrin.common.util

fun List<Any>.hasNothing(): Boolean {
    return this.isEmpty() || get(0).toString().trim() == ""
}