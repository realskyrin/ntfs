package cn.skyrin.ntfs.store

import android.content.Context
import cn.skyrin.common.store.Store
import cn.skyrin.common.store.asStoreProvider

class AppStore(context: Context) {
    private val store = Store(
        context
            .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            .asStoreProvider()
    )

    var firstStartService: Boolean by store.boolean(
        key = "first_start_service",
        defaultValue = true,
    )

    var latestVersionCode: Int by store.int(
        key = "latestVersionCode",
        defaultValue = 0
    )

    var darkTheme: Boolean by store.boolean(
        key = "dark_theme",
        defaultValue = false
    )

    var languageZh: Boolean by store.boolean(
        key = "languageZh",
        defaultValue = true
    )

    companion object {
        private const val FILE_NAME = "app"
    }
}
