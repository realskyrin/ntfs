package cn.skyrin.ntfs.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.skyrin.ntfs.data.bean.DateConvert
import cn.skyrin.ntfs.data.bean.OngoingNotification
import java.util.*

@Database(entities = [OngoingNotification::class], version = 3, exportSchema = false)
@TypeConverters(
    DateConvert::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ongoingNotificationDao(): OngoingNotificationDao

    companion object {
        private const val DATABASE_NAME = "app-db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        // Log.d(TAG, "onOpen: >>>")
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        // Log.d(TAG, "onDestructiveMigration: >>>")
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build()
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 清空表，表结构未改变
                database.execSQL("DELETE FROM OngoingNotification")
            }
        }

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Remove the legacy table
                database.execSQL("DROP TABLE OngoingNotification")
                // Create the new table
                database.execSQL("CREATE TABLE OngoingNotification (id INTEGER NOT NULL, uid TEXT NOT NULL, `key` TEXT NOT NULL, title TEXT, text TEXT,pkg TEXT NOT NULL,label TEXT default null,channel_id TEXT NOT NULL,is_snoozed INTEGER NOT NULL default 0,snooze_at INTEGER,snooze_duration_ms INTEGER NOT NULL default 0,record_at INTEGER,update_at INTEGER,  PRIMARY KEY(id,uid))")
            }
        }
    }

    // 数据库迁移指南
    // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
}
