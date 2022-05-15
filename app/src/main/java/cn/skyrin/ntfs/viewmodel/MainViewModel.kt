package cn.skyrin.ntfs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cn.skyrin.ntfs.data.Repositories
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val ongoingNotifications = Repositories.ongoingNotificationRepository.queryFlow().asLiveData()
    val darkTheme = MutableLiveData(false)
    var languageZh = MutableLiveData<Boolean>()

    fun updateSnoozeStatus(
        uid: String,
        isSnoozed: Boolean,
        snoozeDurationMs: Long,
        date: Date,
    ) = viewModelScope.launch(Dispatchers.IO) {
        Repositories.ongoingNotificationRepository.update(
            uid,
            isSnoozed,
            snoozeDurationMs = snoozeDurationMs,
            snoozeAt = date,
            updateAt = date,
        )
    }
}
