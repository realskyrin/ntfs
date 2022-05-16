package cn.skyrin.ntfs.app

import android.app.Application
import androidx.lifecycle.MutableLiveData
import cn.skyrin.common.Global
import cn.skyrin.common.GlobalMain
import cn.skyrin.ntfs.BuildConfig
import cn.skyrin.ntfs.data.Repositories
import timber.log.Timber

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        Global.init(this)
        GlobalMain.init(this)
        Repositories.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

var isNotificationListenerConnected: MutableLiveData<Boolean> = MutableLiveData(false)
