package cn.skyrin.ntfs.ui.pages

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.skyrin.common.compat.openNotificationServiceSettings
import cn.skyrin.ntfs.R
import cn.skyrin.ntfs.data.bean.OngoingNotification
import cn.skyrin.ntfs.store.Store
import cn.skyrin.ntfs.ui.components.MyTopAppBar
import cn.skyrin.ntfs.ui.theme.NtfsTheme
import cn.skyrin.ntfs.util.toast.showToast
import cn.skyrin.ntfs.viewmodel.MainViewModel
import java.util.*

@Composable
fun HostScreen(
    connected: Boolean,
    notifications: List<OngoingNotification>,
    viewModel: MainViewModel?,
    ctx: Context,
    languageZh: Boolean,
) {
    Box(Modifier.fillMaxSize()) {
        if (connected) {
            NotificationsScreen(
                notifications,
            ) { uid, key ->
                sendSnoozeIntent(uid, key)
                viewModel?.updateSnoozeStatus(uid, true, Date(), snoozeDurationMs)
            }
        } else {
            PermissionScreen {
                ctx.openNotificationServiceSettings()
            }
        }

        MyTopAppBar(
            languageZh = languageZh,
            onDarkThemeClick = {
                Store.store.darkTheme = Store.store.darkTheme.not()
                viewModel?.darkTheme?.postValue(Store.store.darkTheme)
            },
            onLanguageClick = {
                Store.store.languageZh = Store.store.languageZh.not()
                viewModel?.languageZh?.postValue(Store.store.languageZh)
            },
            onSettingsClick = {
                ctx.openNotificationServiceSettings()
            }
        )

        FloatingActionButton(
            onClick = {
                sendRefreshIntent()
                ctx.showToast(R.string.refresh_success)
            },
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier
                .alpha(0.8f)
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "refresh",
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "HostPagePreview"
)
@Composable
fun HostPagePreview() {
    NtfsTheme {
        HostScreen(
            connected = true,
            notifications = listOf(
                OngoingNotification(
                    id = 0,
                    uid = "uidaoisduasdasduad",
                    key = "key",
                    title = "微信通知",
                    text = "微信通知正在后台运行",
                    pkg = "com.tencent.mm",
                    label = "微信",
                    channelId = "10001",
                    isSnoozed = true,
                    snoozeAt = Date(),
                    snoozeDurationMs = 5000,
                    recordAt = Date(),
                    updateAt = Date(),
                ),
                OngoingNotification(
                    id = 1,
                    uid = "uidaoisduasdasduad",
                    key = "key",
                    title = "微信通知",
                    text = "微信通知正在后台运行",
                    pkg = "com.tencent.mm",
                    label = "微信",
                    channelId = "10001",
                    isSnoozed = true,
                    snoozeAt = Date(),
                    snoozeDurationMs = 5000,
                    recordAt = Date(),
                    updateAt = Date(),
                )
            ),
            viewModel = null,
            ctx = LocalContext.current,
            languageZh = true,
        )
    }
}