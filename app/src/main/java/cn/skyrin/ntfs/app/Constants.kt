package cn.skyrin.ntfs.app

import cn.skyrin.ntfs.BuildConfig

object Constants {
    const val TAG = "NTFS"
    const val ACTION_SNOOZE_NOTIFICATION = "cn.skyrin.ntfs.ACTION_SNOOZE_NOTIFICATION"
    const val ACTION_REFRESH_NOTIFICATION = "cn.skyrin.ntfs.ACTION_REFRESH_NOTIFICATION"
    const val EXTRA_NOTIFICATION_UID = "cn.skyrin.ntfs.EXTRA_NOTIFICATION_UID"
    const val EXTRA_NOTIFICATION_KEY = "cn.skyrin.ntfs.EXTRA_NOTIFICATION_KEY"
    const val EXTRA_SNOOZE_DURATION_MS = "cn.skyrin.ntfs.EXTRA_SNOOZE_DURATION_MS"
    val CHANNEL_ID_NOTIFICATION =
        if (BuildConfig.DEBUG) "channel_ntfs_service_debug" else "channel_ntfs_service"
}
