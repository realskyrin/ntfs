package cn.skyrin.ntfs.app

import android.app.Application
import androidx.lifecycle.MutableLiveData
import cn.skyrin.common.Global
import cn.skyrin.common.GlobalMain
import cn.skyrin.ntfs.data.Repositories

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        Global.init(this)
        GlobalMain.init(this)
        Repositories.init(this)
    }
}

var isNotificationListenerConnected: MutableLiveData<Boolean> = MutableLiveData(false)
