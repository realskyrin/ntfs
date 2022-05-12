package cn.skyrin.ntfs.data

import cn.skyrin.ntfs.data.bean.OngoingNotification
import kotlinx.coroutines.flow.Flow
import java.util.*

class OngoingNotificationRepositoryImpl(private val dao: OngoingNotificationDao): OngoingNotificationDao {
    override suspend fun insert(notification: OngoingNotification): Long {
        return dao.insert(notification)
    }

    override suspend fun insert(notifications: List<OngoingNotification>): List<Long> {
        return dao.insert(notifications)
    }

    override suspend fun delete(notification: OngoingNotification): Int {
        return dao.delete(notification)
    }

    override suspend fun delete(notifications: List<OngoingNotification>): Int {
        return dao.delete(notifications)
    }

    override suspend fun delete(uid: String): Int {
        return dao.delete(uid)
    }

    override suspend fun delete() {
        dao.delete()
    }

    override suspend fun update(notification: OngoingNotification): Int {
        return dao.update(notification)
    }

    override suspend fun update(notifications: List<OngoingNotification>): Int {
        return dao.update(notifications)
    }

    override suspend fun update(uid: String, updateAt: Date): Int {
        return dao.update(uid, updateAt)
    }

    override suspend fun update(
        uid: String,
        isSnoozed: Boolean,
        snoozeAt: Date,
        snoozeDurationMs: Long
    ): Int {
        return dao.update(uid, isSnoozed, snoozeAt,snoozeDurationMs)
    }

    override fun queryFlow(): Flow<List<OngoingNotification>> {
        return dao.queryFlow()
    }

    override fun query(uid: String): OngoingNotification? {
        return dao.query(uid)
    }

    override fun size(): Int {
        return dao.size()
    }

    override fun exist(uid: String): Int {
        return dao.exist(uid)
    }
}
