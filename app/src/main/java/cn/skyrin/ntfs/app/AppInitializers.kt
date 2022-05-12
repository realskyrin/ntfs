package cn.skyrin.ntfs.app

import cn.skyrin.ntfs.store.Store

val appInitializers = listOf(
    ::initializeStore,
)

private fun initializeStore() {
    Store.initialize(app)
}
