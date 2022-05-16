package cn.skyrin.ntfs.data

import androidx.room.*
import cn.skyrin.ntfs.data.bean.OngoingNotification
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface OngoingNotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: OngoingNotification): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notifications: List<OngoingNotification>): List<Long>

    @Delete
    suspend fun delete(notification: OngoingNotification): Int

    @Delete
    suspend fun delete(notifications: List<OngoingNotification>): Int

    @Query("delete from OngoingNotification where uid = :uid")
    suspend fun delete(uid: String): Int

    @Query("delete from OngoingNotification")
    suspend fun delete()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(notification: OngoingNotification): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(notifications: List<OngoingNotification>): Int

    @Query("UPDATE OngoingNotification SET update_at= :updateAt WHERE uid =:uid")
    suspend fun update(uid: String, updateAt: Date): Int

    @Query("UPDATE OngoingNotification SET snooze_duration_ms= :snoozeDurationMs,snooze_at= :snoozeAt,update_at= :updateAt,is_snoozed= :isSnoozed WHERE uid =:uid")
    suspend fun update(
        uid: String,
        isSnoozed: Boolean,
        snoozeDurationMs: Long,
        snoozeAt: Date,
        updateAt: Date,
    ): Int

    @Query("UPDATE OngoingNotification SET is_snoozed= :isSnoozed,update_at= :updateAt WHERE uid =:uid")
    suspend fun update(
        uid: String,
        isSnoozed: Boolean,
        updateAt: Date,
    ): Int

    @Query("UPDATE OngoingNotification SET is_snoozed= :isSnoozed,snooze_duration_ms= :snoozeDurationMs,update_at= :updateAt WHERE uid =:uid")
    suspend fun update(
        uid: String,
        isSnoozed: Boolean,
        snoozeDurationMs: Long,
        updateAt: Date,
    ): Int

    @Query("select * from OngoingNotification order by is_snoozed desc")
    fun queryFlow(): Flow<List<OngoingNotification>>

    @Query("select * from OngoingNotification WHERE uid =:uid")
    fun query(uid: String): OngoingNotification?

    @Query("select * from OngoingNotification WHERE update_at < :updateBefore")
    fun query(updateBefore: Long): List<OngoingNotification>

    @Query("select count(*) from OngoingNotification")
    fun size(): Int

    @Query("select count(uid) from OngoingNotification where uid = :uid")
    fun exist(uid: String): Int
}
