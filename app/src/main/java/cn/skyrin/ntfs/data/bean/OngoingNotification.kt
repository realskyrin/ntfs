package cn.skyrin.ntfs.data.bean

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Keep
@Entity(tableName = "OngoingNotification", primaryKeys = ["id", "uid"],)
data class OngoingNotification(
    @ColumnInfo
    var id: Long,
    @ColumnInfo
    var uid: String,
    @ColumnInfo
    var key: String,
    @ColumnInfo
    var title: String?,                         // 通知标题
    @ColumnInfo
    var text: String?,                          // 通知内容
    @ColumnInfo
    var pkg: String,                            // 来源应用 pkg name
    @ColumnInfo
    var label: String? = null,                  // 来源应用 app name
    @ColumnInfo(name = "channel_id")
    var channelId: String,                      // 来源消息 channel id
    @ColumnInfo(name = "is_snoozed")
    var isSnoozed: Boolean,                     // 是否已经冻结
    @ColumnInfo(name = "snooze_at")
    var snoozeAt: Date?,                        // 通知冻结时间
    @ColumnInfo(name = "snooze_duration_ms")
    var snoozeDurationMs: Long,                 // 通知冻结时长
    @ColumnInfo(name = "record_at")
    var recordAt: Date?,                        // 通知录入时间
    @ColumnInfo(name = "update_at")
    var updateAt: Date?,                        // 通知更新时间
) {
    companion object {
        fun areTheSame(
            left: OngoingNotification,
            right: OngoingNotification,
        ): Boolean {
            return right.id == left.id
        }

        fun areContentsTheSame(
            left: OngoingNotification,
            right: OngoingNotification,
        ): Boolean {
            return right.title == left.title
                    && right.text == left.text
                    && right.recordAt == left.recordAt
        }
    }
}

class DateConvert {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
