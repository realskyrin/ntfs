package cn.skyrin.ntfs.data

import android.app.Application

object Repositories {
  private lateinit var context: Application

  val ongoingNotificationRepository by lazy { OngoingNotificationRepositoryImpl(AppDatabase.getInstance(context).ongoingNotificationDao()) }

  fun init(application: Application) {
    context = application
  }
}
