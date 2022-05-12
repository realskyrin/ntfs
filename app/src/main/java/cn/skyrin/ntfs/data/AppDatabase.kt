package cn.skyrin.ntfs.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.skyrin.ntfs.data.bean.DateConvert
import cn.skyrin.ntfs.data.bean.OngoingNotification

@Database(entities = [OngoingNotification::class], version = 2, exportSchema = false)
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
                .build()
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DELETE FROM OngoingNotification")
            }
        }
    }
}