package cn.skyrin.ntfs.store

import android.app.Application

object Store {
    lateinit var store: AppStore private set

    fun initialize(application: Application) {
        store = AppStore(application)
    }
}
