package cn.skyrin.ntfs.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import cn.skyrin.common.compat.getAppName
import cn.skyrin.common.util.md5
import cn.skyrin.ntfs.R
import cn.skyrin.ntfs.app.Constants
import cn.skyrin.ntfs.app.isNotificationListenerConnected
import cn.skyrin.ntfs.data.AppDatabase
import cn.skyrin.ntfs.data.bean.OngoingNotification
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.util.*

class NtfsService : NotificationListenerService() {
    private val appDatabase by lazy { AppDatabase.getInstance(this) }
    private val snoozeReceiver by lazy { SnoozeReceiver() }
    private val jobs = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + jobs)
    private val mutex = Mutex()

    override fun onDestroy() {
        super.onDestroy()
        jobs.cancel()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        isNotificationListenerConnected.postValue(true)

        kotlin.runCatching {
            val svIntentFilter = IntentFilter()
            svIntentFilter.addAction(Constants.ACTION_SNOOZE_NOTIFICATION)
            svIntentFilter.addAction(Constants.ACTION_REFRESH_NOTIFICATION)
            registerReceiver(snoozeReceiver, svIntentFilter)
        }

        collectAllOngoingNotifications()
    }

    private fun collectAllOngoingNotifications(refresh: Boolean = false) {
        activeNotifications.forEach {
            if (isOngoingNotification(it)) {
                collectNotifications(it, false)
            }
        }

        snoozedNotifications.forEach {
            if (isOngoingNotification(it)) {
                collectNotifications(it, true)
            }
        }


        if (refresh) {
            scope.launch {
                delay(1000)
                // 删除未更新的通知，即不存在的通知
                val expired =
                    appDatabase.ongoingNotificationDao().query(updateBefore = Date().time - 1500)
                appDatabase.ongoingNotificationDao().delete(expired)
            }
        }
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        isNotificationListenerConnected.postValue(false)

        kotlin.runCatching {
            unregisterReceiver(snoozeReceiver)
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (isOngoingNotification(sbn) && isDuplicateNotification(sbn).not()) {
            collectNotifications(sbn!!, false)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        if (isOngoingNotification(sbn)) {
            removeOngoingNotification(sbn!!)
        }
    }

    private fun removeOngoingNotification(sbn: StatusBarNotification) = scope.launch {
        val title = sbn.notification.extras.get("android.title").toString()
        val text = sbn.notification.extras.get("android.text").toString()
        val uid = generateNotificationUid(sbn.key, title, text)

        mutex.withLock {
            Timber.d("removeOngoingNotification: $uid")
            // 如果是手动休眠引起的 remove，则不删除
            if (manualSnoozedNotificationUid.contains(uid)) {
                manualSnoozedNotificationUid.remove(uid)
            } else {
                Timber.d("appDatabase.ongoingNotificationDao().delete(uid): $uid")
                appDatabase.ongoingNotificationDao().delete(uid)
            }
        }
    }

    private fun collectNotifications(
        sbn: StatusBarNotification,
        isSnoozed: Boolean = false,
    ) = scope.launch {

        val key = sbn.key
        val title = sbn.notification.extras.get("android.title").toString()
        val text = sbn.notification.extras.get("android.text").toString()
        val uid = generateNotificationUid(key, title, text)

        if (appDatabase.ongoingNotificationDao().exist(uid) > 0) {
            if (isSnoozed) {
                appDatabase.ongoingNotificationDao()
                    .update(uid, isSnoozed, updateAt = Date())
            } else {
                appDatabase.ongoingNotificationDao()
                    .update(uid, isSnoozed, 0L, updateAt = Date())
            }
        } else {
            OngoingNotification(
                id = 0,
                uid = uid,
                key = key,
                title = if ("null" == title.lowercase()) getString(R.string.no_content_to_show) else title,
                text = if ("null" == text.lowercase()) getString(R.string.no_content_to_show) else text,
                pkg = sbn.packageName,
                label = getAppName(sbn.packageName),
                channelId = sbn.notification.channelId,
                isSnoozed = isSnoozed,
                snoozeAt = Date(),
                snoozeDurationMs = 0,
                recordAt = Date(sbn.postTime),
                updateAt = Date(),
            ).also { notification ->
                appDatabase.ongoingNotificationDao().insert(notification)
            }
        }
    }

    /**
     * @param key 0|com.tencent.mm|1390562710|null|10245
     */
    private fun generateNotificationUid(key: String, title: String, text: String) =
        key.plus(title).plus(text).md5()

    var lastPostTime = 0L
    var lastKey = ""
    var lastUid = ""

    /**
     * 同一内容同一时间 视为重复通知
     */
    private fun isDuplicateNotification(
        sbn: StatusBarNotification?
    ): Boolean {
        if (sbn == null) return false

        val key = sbn.key
        val postTime = sbn.postTime
        val title = sbn.notification.extras.get("android.title").toString()
        val text = sbn.notification.extras.get("android.text").toString()

        val uid = generateNotificationUid(key = key, title = title, text = text)
        val b = (key == lastKey && postTime == lastPostTime) || (uid == lastUid)
        lastKey = key
        lastPostTime = postTime
        lastUid = uid
        return b
    }

    /**
     * 是否是固定通知
     */
    private fun isOngoingNotification(sbn: StatusBarNotification?) =
        sbn?.isClearable?.not() ?: false

    var manualSnoozedNotificationUid = mutableSetOf<String>()

    inner class SnoozeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == Constants.ACTION_REFRESH_NOTIFICATION) {
                collectAllOngoingNotifications(refresh = true)
            }

            if (intent.action == Constants.ACTION_SNOOZE_NOTIFICATION) {
                val uid4Snooze = intent.getStringExtra(Constants.EXTRA_NOTIFICATION_UID) ?: return
                val key4Snooze = intent.getStringExtra(Constants.EXTRA_NOTIFICATION_KEY) ?: return
                val snoozeDurationMs = intent.getLongExtra(Constants.EXTRA_SNOOZE_DURATION_MS, 0)

                kotlin.runCatching {
                    Timber.d("manualSnoozedNotificationUid.add(uid4Snooze)")
                    manualSnoozedNotificationUid.add(uid4Snooze)
                    snoozeNotification(key4Snooze, snoozeDurationMs)
                }
            }
        }
    }
}
